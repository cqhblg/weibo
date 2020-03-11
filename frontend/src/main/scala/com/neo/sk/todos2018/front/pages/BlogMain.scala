package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.{addRecord, blogList, deleteRecord, getCommentButton, getDeleteButton, getLikesButton, getList}
import com.neo.sk.todos2018.front.styles.ListStyles._
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.{CommonRsp, SuccessRsp}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.{UserLoginReq, UserLoginRsp}
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddBlogCommentReq, AddRecordReq, BlogComment, BlogRecord, DelBlogComment, DelRecordReq, GetCommentListRsp, GetListRsp, GiveAttentionRX, LikesRecordReq}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import mhtml.Var
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.xml.Node
import scala.concurrent.ExecutionContext.Implicits.global

object BlogMain {
  val url = "#/" + "Blog"

  val blogList = Var(List.empty[BlogRecord])
  val commentList=Var(List.empty[BlogComment])

  def getDeleteButton(id: Int,username:String) =  <button class={deleteButton.htmlClass} onclick={()=>deleteComment(id,username)}>删除</button>

  def deleteComment(id: Int, author:String): Unit = {
    val data = DelBlogComment(id,author).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.Blog.deleteBlogComment, data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("已删除！")
          getBlogComment
        } else {
          JsFunc.alert(rsp.msg)
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"parse error,$error")
    }
  }
  //添加关注
  def giveAttention(author:String): Unit ={
    val data=GiveAttentionRX(author).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.Blog.giveAttention,data).map{
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("已关注")
        } else {
          JsFunc.alert(rsp.msg)
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"parse error,$error")
    }
  }
//获取当前博客的内容
  def getBlogContent: Unit = {
    Http.getAndParse[GetListRsp](Routes.Blog.getBlogContent).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          blogList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          //         dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get blog list error,$error")
    }
  }
  //获取当前博客的评论
  def getBlogComment: Unit = {
    Http.getAndParse[GetCommentListRsp](Routes.Blog.getBlogComment).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          commentList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          //         dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get blog list error,$error")
    }
  }
  val blogContentRx=blogList.map{
    case Nil=> <div style ="margin: 30px; font-size: 17px;">未获取到动态</div>
    case list =>  <div class="mdui-card mdui-center question">
      {list.map { l =>
        <div>
        <div>
          <div class="mc-user-line">
            <a class="avatar" style="background-image: url(&quot;https://www.mdui.org/upload/avatar/ff/12/0874da5deda2ae122d9c46d5a8e4bdb0_m.png&quot;);"></a>
            <div class="info">
              <div class="username">
                <a >{l.username}</a>
              </div>
              <div class="headline"></div>
            </div>
            <div class="more">
              <span class="time" >{TimeTool.dateFormatDefault(l.time)}</span>
            </div>
          </div>
          <div class="mdui-typo content">
            <p>{l.content}</p>
          </div>
          <div class="actions">
            <button class="mdui-btn mdui-btn-raised mdui-text-color-blue" onclick={()=>giveAttention(l.username)}>关注</button>

            <button class="mdui-btn mdui-btn-raised mdui-color-theme-accent" onclick={()=>likesRecord(l.id,l.username)}><i class="mdui-icon material-icons">&#xe8dc;</i></button>

          </div>
        </div>
          <div class="mc-loading mdui-spinner mdui-center mdui-m-y-3 mdui-hidden">
            <div class="mdui-spinner-layer ">
              <div class="mdui-spinner-circle-clipper mdui-spinner-left">
                <div class="mdui-spinner-circle"></div>
              </div>
              <div class="mdui-spinner-gap-patch">
                <div class="mdui-spinner-circle"></div>
              </div>
              <div class="mdui-spinner-circle-clipper mdui-spinner-right">
                <div class="mdui-spinner-circle"></div>
              </div>
            </div>
          </div>
        </div>
        }
      }
    </div>

  }
  val commentListTable = commentList.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无评论</div>
    case list => <div class="mdui-card mdui-center answers">
    {list.map {l =>
        <div class="item">
          <div class="mc-user-line">
            <a class="avatar"  style="background-image: url(&quot;https://www.mdui.org/upload/avatar/67/0d/fd346c88f81be5b0b09296bf5ef52bb2_m.png&quot;);"></a>
            <div class="info">
              <div class="username">
                <a >{l.author}</a>
              </div>
              <div class="headline"></div>
            </div>
            <div class="more">
              <span class="time" >{TimeTool.dateFormatDefault(l.time)}</span>
            </div>
          </div>
          <div class="content mdui-typo">
            <p>{l.content}</p>
          </div>
          <div>
            <button class="mdui-btn mdui-btn-raised mdui-text-color-black" onclick={()=>deleteComment(l.comment_id,l.author)}>删除评论</button>
          </div>
        </div>

      }
        }</div>
  }
  def likesRecord(id: Int,username:String): Unit = {
    val data = LikesRecordReq(id,username).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.likesRecord, data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("点赞成功！")

        } else {
          JsFunc.alert(rsp.msg)
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"parse error,$error")
    }
  }
  def addComment: Unit = {
    val data = dom.document.getElementById("inputComment").asInstanceOf[Input].value
    if (data == ""){
      JsFunc.alert("评论不能为空！")
    }
    else{
      Http.postJsonAndParse[SuccessRsp](Routes.Blog.addBlogComment, AddBlogCommentReq(data).asJson.noSpaces).map {
        case Right(rsp) =>
          if(rsp.errCode == 0) {
            JsFunc.alert("成功添加评论！")
            getBlogComment
          } else {
            JsFunc.alert("评论失败！")
            println(rsp.msg)
          }

        case Left(error) =>
          println(s"parse error,$error")
      }
    }
  }

  def app: Node = {
    getBlogContent
    getBlogComment
    <div id="page-question" class="mdui-container">
       {blogContentRx}
      <div class="mdui-typo-headline answers-count"></div>
     {commentListTable}
      <div>
        <input id="inputComment" class={inputComment.htmlClass}></input>
      <button class={addButton.htmlClass} onclick={()=>addComment}>发布评论</button>
      </div>

  </div>
  }
}
