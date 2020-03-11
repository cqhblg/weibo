package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.{CommonRsp, SuccessRsp}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.{UserLoginReq, UserLoginRsp}
import com.neo.sk.todos2018.shared.ptcl.RegisterProtocol.UserRegisterReq
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.xml.Node
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * User: ChengQi
  * Date: 2019/9/20
  * Time: 17:40
  */
object Register {
  val url = "#/" + "Register"

  private def userRegister(): Unit ={
    val userName = dom.document.getElementById("userName").asInstanceOf[Input].value
    val password = dom.document.getElementById("userPassword").asInstanceOf[Input].value
    val passwordagain=dom.document.getElementById("userPasswordAgain").asInstanceOf[Input].value
    if(userName.length()==0) JsFunc.alert("用户名不能为空")
      else if(password.length==0) JsFunc.alert("密码不能为空")
      else if(password!=passwordagain) JsFunc.alert("两次输入密码不一致")
      else {Http.postJsonAndParse[SuccessRsp](Routes.Register.userRegister, UserRegisterReq(userName, password).asJson.noSpaces).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          JsFunc.alert(s"注册成功")
          dom.window.location.hash = "/Login"
        }
        else{
          JsFunc.alert(s"注册失败：${rsp.msg}")
        }
      case Left(error) =>
        JsFunc.alert(s"parse error,$error")
    }
  }}

  def app: Node =
    <div>
      <div class = "LoginForm">
        <h2>注册页面</h2>
        <div class = "inputContent">
          <span>用户名</span>
          <input id = "userName"></input>
        </div>
        <div class = "inputContent">
          <span>密码</span>
          <input id = "userPassword" type = "password"></input>
        </div>
        <div class = "inputContent">
          <span>再次输入密码</span>
          <input id = "userPasswordAgain" type = "password"></input>
        </div>
        <button onclick = {()=> userRegister()}>注册</button>
      </div>
    </div>
}
