package com.neo.sk.todos2018.front.styles
import scala.language.postfixOps
import scalacss.DevDefaults._
/**
  * Created by haoshuhan on 2018/6/4.
  */
object ListStyles extends StyleSheet.Inline{
  import dsl._
  val th = style(
    textAlign.center,
    padding(0 px, 26 px)
  )
  val td = style(
    textAlign.center
  )
  val inputBlog = style(

    alignSelf.center,
    textAlign.left,
    width(300 px),
    height(200 px),
    borderRadius(5 px),
    fontSize(17 px),
    borderWidth(1 px),
    borderColor(red),
    borderStyle(dotted)
  )

  val addButton = style(
    width(100 px),
    height(38 px),
    borderRadius(5 px),
    fontSize(17 px)
  )

  val deleteButton = style(
    width(60 px),
    height(30 px),
    borderRadius(5 px),
    fontSize(15 px)
  )

  val blogContent= style(
    width(400 px),
    height(200 px),
    fontSize(17 px),
    borderWidth(1 px),
    borderColor(blue),
    borderStyle(dotted)
  )
  val inputComment=style(

    width(200 px),
    height(30 px),
    borderRadius(5 px),
    fontSize(17 px),
    borderWidth(1 px),
    borderColor(red),
    borderStyle(dotted)
  )

  val logoutButton = addButton + style(float.right, marginRight(5.%%))
}
