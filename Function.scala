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

object Utilities {
  def createReg[A <: Data](clock: Clock, tpe: A): A = {
    enterScope(this, "createReg")
    exitScope(withClock(clock) {
      Reg(tpe)
    })
  }
}

class Function extends RawModule {
  enterScope(this)

  val clock = IO(Input(Clock()))
  val a = IO(Input(Bool()))
  val b = IO(Output(Bool()))

  val r1 = Utilities.createReg(clock, chiselTypeOf(a))
  val r2 = Utilities.createReg(clock, chiselTypeOf(a))

  r1 := a
  r2 := r1
  b := r2

  exitScope()
}

object Main extends App {
  (new ChiselStage).execute(
    Array("--target-dir", "build/", "--target", "systemverilog"),
    Seq(ChiselGeneratorAnnotation(() => new Function))
  )
}
