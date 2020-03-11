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
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, UserItem}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: XuSiRan
  * Date: 2019/3/26
  * Time: 19:15
  */
trait LoginService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val userLogin = (path("userLogin") & post){
    entity(as[Either[Error, UserLoginReq]]){    //left:Error,right:UserLoginReq
      case Left(error) =>
        log.warn(s"error in userLogin: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult{
        ToDoListDAO.judgeUser(req.userName,req.password).map(
          r=>if(r.nonEmpty){
            val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
            addSession(session.toSessionMap) {
              complete(SuccessRsp())
            }
          }else{
              complete(ErrorRsp(1000101, "用户名或密码不正确"))
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

  val loginRoutes: Route =
    pathPrefix("login") {
      userLogin ~ userLogout
    }

}
