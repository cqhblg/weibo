package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.commentRecord
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, AttentionUser, BlogIdReq, BlogRecord, DelRecordReq, GetAttentionUserRsp, GetListRsp, LikesRecordReq, TaskRecord}
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import com.neo.sk.todos2018.front.styles.ListStyles._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global
object BlogAttention {
  val url = "#/" + "BlogAttention"

  val AttentionUserList = Var(List.empty[AttentionUser])



  def getList: Unit = {
    Http.getAndParse[GetAttentionUserRsp](Routes.List.getAttentionUser).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          AttentionUserList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get blog list error,$error")
    }
  }



  def goToBlog(author:String): Unit ={
    dom.window.location.hash = "/BlogOther/"+author
  }
  val userListRx = AttentionUserList.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂未关注任何用户</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <div id="page-questions" class="mdui-container">
        {list.map {l =>
        <a class="item" onclick={()=>goToBlog(l.username)}>
          <div class="avatar" style="background-image: url(&quot;https://www.mdui.org/upload/avatar/07/1f/17e842e68eff0ab44627726d56288502_m.png&quot;);"></div>
          <div class="content">
            <div class="title">
              {l.username}
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
          AttentionUserList := Nil
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
    getList
    <div>
      <div style="margin:30px;font-size:25px;">我关注的用户</div>
      <div>
        <button class={logoutButton.htmlClass} onclick={()=>logout()}>退出</button></div>
      {userListRx}
    </div>
  }
}
