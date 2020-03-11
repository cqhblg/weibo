package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.ptcl.UserProtocol.UserBaseInfo
import com.neo.sk.todos2018.service.SessionBase.{SessionKeys, SessionTypeKey, ToDoListSession}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.UserLoginReq
import com.neo.sk.todos2018.shared.ptcl.RegisterProtocol.UserRegisterReq
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, UserItem}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

trait RegisterService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val userRegister = (path("userRegister") & post){
    entity(as[Either[Error, UserRegisterReq]]){    //left:Error,right:UserLoginReq
      case Left(error) =>
        log.warn(s"error in userLogin: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult{
          ToDoListDAO.registerUser(req.userName,req.password).map(
            r=>if(r>0){
              val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
              addSession(session.toSessionMap) {
                complete(SuccessRsp())
              }
            }else{
              complete(ErrorRsp(1000101, "注册失败"))
            }
          )
        }
    }
  }
  private val userLogout = (path("userLogout") & get) {
    userAuth{ _ =>
      val session=Set(SessionTypeKey,SessionKeys.name)
      removeSession(session){ctx =>
        ctx.complete(SuccessRsp())
      }
    }
  }

  val RegisterRoutes: Route =
    pathPrefix("register") {
      userRegister
    }

}


