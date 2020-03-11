package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.BlogProtocol.BlogBaseInfo
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.ptcl.UserProtocol.UserBaseInfo
import com.neo.sk.todos2018.service.SessionBase.{BlogSession, ToDoListSession}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.UserLoginReq
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, AttentionUser, BlogIdReq, BlogRecord, DelRecordReq, GetAttentionUserRsp, GetListRsp, GetOtherBlog, LikesRecordReq, TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:41
  * 2019/3/21 delete session check
  */
trait ToDoListService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addRecord = (path("addRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, AddRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            val author = user.userName
            println(s"add record: ${req.content}")
            ToDoListDAO.addBlog(author, req.content).map { r =>
              if (r > 0) {
                complete(SuccessRsp())
              } else {
                complete(ErrorRsp(1000101, "add record error"))
              }
            }
          }
      }
    }
  }

  private val delRecord = (path("delRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, DelRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          if(req.username!=user.userName){
            complete(ErrorRsp(1000101, "无法删除非本人发出的动态！"))
          }else{
            dealFutureResult {
              val id = req.id
              println(s"delete record: $id")
              ToDoListDAO.delBlog(id).map { r =>
                if (r > 0) {
                  complete(SuccessRsp())
                } else {
                  complete(ErrorRsp(1000101, "add record error"))
                }
              }
            }
          }
      }
    }
  }

  private val getOtherList = (path("getOtherList") & post) {
    entity(as[Either[Error,GetOtherBlog]]) {
      case Left(error) =>
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult(
          ToDoListDAO.getListByAuthor(req.name).map { list =>
            val data = list.map( r => BlogRecord(r.id,r.userName,r.blogContent,r.time)).toList
            complete(GetListRsp(Some(data)))
          }
        )
    }
  }

  private val getBlog = (path("getList") & get) {
    userAuth{ user =>
      dealFutureResult(
        ToDoListDAO.getBlogList().map { list =>
          val data = list.map( r => BlogRecord(r.id,r.userName,r.blogContent,r.time)).toList
          complete(GetListRsp(Some(data)))
        }
      )
    }
  }
  private val getSelfBlog = (path("getSelfList") & get) {
    userAuth{ user =>
      dealFutureResult(
        ToDoListDAO.getListByAuthor(user.userName).map { list =>
          val data = list.map( r => BlogRecord(r.id,r.userName,r.blogContent,r.time)).toList
          complete(GetListRsp(Some(data)))
        }
      )
    }
  }
  private val getAttentionUser = (path("getAttentionUser") & get) {
    userAuth{ user =>
      dealFutureResult(
        ToDoListDAO.getAttentionUser(user.userName).map { list =>
          val data = list.map( r => AttentionUser(r)).toList
          complete(GetAttentionUserRsp(Some(data)))
        }
      )
    }
  }

  private val addLikes = (path("likesRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, LikesRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            println(s"add likes: ${req.username}")
            ToDoListDAO.addLikes(req.id,user.userName).map { r =>
              if (r > 0) {
                complete(SuccessRsp())
              } else {
                complete(ErrorRsp(1000101, "add Likes error"))
              }
            }
          }
      }
    }
  }
//  private val userLogin = (path("userLogin") & post){
//    entity(as[Either[Error, UserLoginReq]]){    //left:Error,right:UserLoginReq
//      case Left(error) =>
//        log.warn(s"error in userLogin: $error")
//        complete(parseError)
//      case Right(req) =>
//        dealFutureResult{
//          ToDoListDAO.judgeUser(req.userName,req.password).map(
//            r=>if(r.nonEmpty){
//              val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
//              addSession(session.toSessionMap) {
//                complete(SuccessRsp())
//              }
//            }else{
//              complete(ErrorRsp(1000101, "用户名或密码不正确"))
//            }
//          )
//        }
//    }
//  }
  private val commentBlog=(path("commentRecord") & post){
      entity(as[Either[Error,BlogIdReq]]){    //left:Error,right:UserLoginReq
        case Left(error) =>
          log.warn(s"error in userLogin: $error")
//          println("wrong~~~~~~~")
          complete(parseError)
        case Right(req) =>
          val session = BlogSession(BlogBaseInfo(req.id), System.currentTimeMillis())
          addSession(session.toSessionMap) {
            complete(SuccessRsp())
          }
      }
  }

  val listRoutes: Route =
    pathPrefix("list") {
      addRecord ~ delRecord ~ getBlog ~ addLikes ~ commentBlog ~ getSelfBlog ~ getAttentionUser ~getOtherList
    }

}
