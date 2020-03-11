package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddBlogCommentReq, AddRecordReq, BlogComment, BlogRecord, DelBlogComment, DelRecordReq, GetCommentListRsp, GetListRsp, GiveAttentionRX, LikesRecordReq, TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

trait BlogService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addComment = (path("addBlogComment") & post) {
    userAuth{ user =>
      blogAuth { blog =>
        entity(as[Either[Error, AddBlogCommentReq]]) {
          case Left(error) =>
            log.warn(s"some error: $error")
            complete(parseError)
          case Right(req) =>
            dealFutureResult {
              val author = user.userName
              println(s"add comment: ${req.content}")
              ToDoListDAO.addBlogComment(blog.id,author, req.content).map { r =>
                if (r > 0) {
                  complete(SuccessRsp())
                } else {
                  complete(ErrorRsp(1000101, "add comment error"))
                }
              }
            }
        }
      }
    }
  }

  private val delBlogComment = (path("deleteBlogComment") & post) {
    userAuth{ user =>
      entity(as[Either[Error, DelBlogComment]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          if(req.author !=user.userName){
            complete(ErrorRsp(1000101, "无法删除非本人发出的评论！"))
          }else{
            dealFutureResult {
              val id = req.id
              println(s"delete comment: $id")
              ToDoListDAO.delBlogComment(id).map { r =>
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


  private val getBlogContent = (path("getBlogContent") & get) {
    blogAuth{ blog =>
      dealFutureResult(
        ToDoListDAO.getBlogInfoById(blog.id).map { list =>
          val data = list.map( r => BlogRecord(r.id,r.userName,r.blogContent,r.time)).toList
          println("getBlogContent")
          complete(GetListRsp(Some(data)))
        }
      )
    }
  }
  private val getBlogComment = (path("getBlogComment") & get) {
    blogAuth{ blog =>
      dealFutureResult(
        ToDoListDAO.getBlogCommentById(blog.id).map { list =>
          val data = list.map( r => BlogComment(r.commentId,r.commentUser,r.commentContent,r.time)).toList
          println("getBlogComment")
          complete(GetCommentListRsp(Some(data)))
        }
      )
    }
  }
  private val giveAttention = (path("giveAttention") & post) {
    userAuth{ user =>
      entity(as[Either[Error, GiveAttentionRX]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            println(s"add attention: ${req.author}")
            ToDoListDAO.addAttention(user.userName,req.author).map { r =>
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

  val blogRoutes: Route =
    pathPrefix("blog") {
      getBlogContent ~ getBlogComment ~ addComment ~ delBlogComment~giveAttention
    }

}
