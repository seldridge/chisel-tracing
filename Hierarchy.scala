//> using scala "2.13.8"
//> using lib "edu.berkeley.cs::chisel3::3.6.0-M2"
//> using plugin "edu.berkeley.cs:::chisel3-plugin::3.6.0-M2"

import chisel3._
import circt.stage.{ChiselStage, FirtoolOption}
import chisel3.stage.ChiselGeneratorAnnotation

object State {
  private var indent: String = ""
  private def incIndent() = {
    indent = indent + "  "
  }
  private def decIndent() = {
    indent = indent.dropRight(2)
  }
  def enterScope[A](a: A, extra: String = "") = {
    println(s"${indent}- ${a.getClass.getSimpleName}$extra")
    incIndent()
  }
  def exitScope[A](a: A): A = {
    decIndent()
    a
  }
}

import State._

class Bar extends RawModule {
  enterScope(this)

  val a = IO(Input(Bool()))
  val b = IO(Output(Bool()))
  b := a

  exitScope()
}

class Foo extends RawModule {
  enterScope(this)

  val a = IO(Input(Bool()))
  val b = IO(Output(Bool()))
  val bar1 = Module(new Bar)
  val bar2 = Module(new Bar)
  bar1.a := a
  bar2.a := bar1.b
  b := bar2.b

  exitScope()
}

class Hierarchy extends RawModule {
  enterScope(this)

  val a = IO(Input(Bool()))
  val b = IO(Output(Bool()))

  val foo1 = Module(new Foo)
  val foo2 = Module(new Foo)
  foo1.a := a
  foo2.a := foo1.b
  b := foo2.b

  exitScope()
}

object Main extends App {
  (new ChiselStage).execute(
    Array("--target-dir", "build/", "--target", "systemverilog"),
    Seq(ChiselGeneratorAnnotation(() => new Hierarchy))
  )
}
