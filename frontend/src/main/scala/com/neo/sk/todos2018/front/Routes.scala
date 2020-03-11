package com.neo.sk.todos2018.front

/**
  * Created by haoshuhan on 2018/6/4.
  */
object Routes {
  val base = "/todos2018"

  object Login{
    val baseUrl = base + "/login"
    val userLogin = baseUrl + "/userLogin"
    val userLogout = baseUrl + "/userLogout"
  }

  object Register{
    val baseUrl = base + "/register"
    val userRegister = baseUrl + "/userRegister"
    val userRegout = baseUrl + "/userRegister"
  }

  object List {
    val baseUrl = base + "/list"
    val getAllList = baseUrl + "/getList"
    val getSelfList= baseUrl+"/getSelfList"
    val getOtherList=baseUrl+"/getOtherList"
    val getAttentionUser=baseUrl+"/getAttentionUser"
    val addRecord = baseUrl + "/addRecord"
    val delRecord = baseUrl + "/delRecord"
    val likesRecord=baseUrl+"/likesRecord"
    val commentRecord=baseUrl+"/commentRecord"
  }
  object Blog{
    val baseUrl=base + "/blog"
    val getBlogContent=baseUrl+"/getBlogContent"
    val getBlogComment=baseUrl+"/getBlogComment"
    val deleteBlogComment=baseUrl+"/deleteBlogComment"
    val addBlogComment=baseUrl+"/addBlogComment"
    val giveAttention=baseUrl+"/giveAttention"
  }

}
