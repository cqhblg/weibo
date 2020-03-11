package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, BlogIdReq, BlogRecord, DelRecordReq, GetListRsp, LikesRecordReq, TaskRecord}
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import com.neo.sk.todos2018.front.styles.ListStyles._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by haoshuhan on 2018/6/4.
  * changed by Xu Si-ran on 2019/3/21
  * update by zhangtao, 2019-3-23: record id.
  */
object TaskList{

  val url = "#/" + "List"

  val blogList = Var(List.empty[BlogRecord])

  def getDeleteButton(id: Int,username:String) =  <button class={deleteButton.htmlClass} onclick={()=>deleteRecord(id,username)}>删除</button>
  def getCommentButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>commentRecord(id)}>评论</button>
  def getLikesButton(id: Int,username:String) =  <button class={deleteButton.htmlClass} onclick={()=>likesRecord(id,username)}>点赞</button>

  def addRecord: Unit = {
    val data = dom.document.getElementById("taskInput").asInstanceOf[Input].value
    if (data == ""){
      JsFunc.alert("输入框不能为空！")
    }
    else{
      Http.postJsonAndParse[SuccessRsp](Routes.List.addRecord, AddRecordReq(data).asJson.noSpaces).map {
        case Right(rsp) =>
          if(rsp.errCode == 0) {
            JsFunc.alert("添加成功！")
            getList
          } else {
            JsFunc.alert("添加失败！")
            println(rsp.msg)
          }

        case Left(error) =>
          println(s"parse error,$error")
      }
    }
  }

  def deleteRecord(id: Int,username:String): Unit = {
    val data = DelRecordReq(id,username).asJson.noSpaces
    println("~~~~~~~~~"+data)
    Http.postJsonAndParse[SuccessRsp](Routes.List.delRecord, data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("已删除！")
          getList
        } else {
          JsFunc.alert(rsp.msg)
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"parse error,$error")
    }
  }

  def commentRecord(id: Int): Unit = {
    val data = BlogIdReq(id).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.commentRecord, data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          dom.window.location.hash = "/Blog"
        } else {
          JsFunc.alert("无法进入该动态")
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"parse error,$error")
    }
  }

  def likesRecord(id: Int,username:String): Unit = {
    val data = LikesRecordReq(id,username).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.likesRecord, data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("点赞成功！")
          getList
        } else {
          JsFunc.alert(rsp.msg)
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"parse error,$error")
    }
  }

  def getList: Unit = {
    Http.getAndParse[GetListRsp](Routes.List.getAllList).map {
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
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无热门消息</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <div id="page-questions" class="mdui-container">
        {list.map {l =>
        <a class="item"  onclick={()=>commentRecord(l.id)}>
          <div class="avatar" style="background-image: url(&quot;https://www.mdui.org/upload/avatar/07/1f/17e842e68eff0ab44627726d56288502_m.png&quot;);"></div>
          <div class="content">
            <div class="title">
              {l.content}
            </div>
            <div class="meta">
              <div class="username">
                {l.username}
              </div>
              <div class="answer_time" title="2019-09-03 16:27:20">
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
   getList
  <div>
    <div style="margin:30px;font-size:25px;">热门推荐</div>
    <div class="mdui-textfield mdui-textfield-floating-label">
      <label class="mdui-textfield-label">说点什么...</label>
      <textarea id ="taskInput" class="mdui-textfield-input"></textarea>
    </div>
    <div>
      <button class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-theme-accent" onclick={()=>addRecord}>发布</button>
    </div>
    <div>
      <button class={logoutButton.htmlClass} onclick={()=>logout()}>退出</button></div>

    {taskListRx}
  </div>
  }

}
