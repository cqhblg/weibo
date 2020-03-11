package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.commentRecord
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, BlogIdReq, BlogRecord, DelRecordReq, GetListRsp, GetOtherBlog, LikesRecordReq, TaskRecord}
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import com.neo.sk.todos2018.front.styles.ListStyles._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global
case class BlogOther(author:String){
  val url = "#/" + "BlogOther"

  val blogList = Var(List.empty[BlogRecord])



  def getOtherList: Unit = {
    val data=GetOtherBlog(author).asJson.noSpaces
    Http.postJsonAndParse[GetListRsp](Routes.List.getOtherList,data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          blogList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get blog list error,$error")
    }
  }



  val taskListRx = blogList.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂未发出任何动态</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <div id="page-questions" class="mdui-container">
        {list.map {l =>
        <a class="item" onclick={()=>commentRecord(l.id)}>
          <div class="avatar" style="background-image: url(&quot;https://www.mdui.org/upload/avatar/07/1f/17e842e68eff0ab44627726d56288502_m.png&quot;);"></div>
          <div class="content">
            <div class="title">
              {l.content}
            </div>
            <div class="meta">
              <div class="username">
                {l.username}
              </div>
              <div class="answer_time" >
                发布于 {TimeTool.dateFormatDefault(l.time)}
              </div>
            </div>
          </div>
        </a>
      }
        }

      </div>

    </div>
  }

  def logout(): Unit = {
    Http.getAndParse[SuccessRsp](Routes.Login.userLogout).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          JsFunc.alert("退出成功")
          blogList := Nil
          dom.window.location.hash = "/Login"
        }
        else{
          JsFunc.alert(s"退出失败：${rsp.msg}")
        }
      case Left(error) =>
        JsFunc.alert(s"parse error,$error")
    }
  }

  def app: xml.Node = {
    getOtherList
    <div>
      <div style="margin:30px;font-size:25px;">ta的动态</div>
      <div>
        <button class={logoutButton.htmlClass} onclick={()=>logout()}>退出</button></div>
      {taskListRx}
    </div>
  }
}
