//> using scala "2.13.8"
//> using lib "edu.berkeley.cs::chisel3::3.6.0-M2"
//> using plugin "edu.berkeley.cs:::chisel3-plugin::3.6.0-M2"

import chisel3._
import circt.stage.{ChiselStage, FirtoolOption}
import chisel3.stage.ChiselGeneratorAnnotation

class Basic extends RawModule {
  val a = IO(Input(Bool()))
  val b = IO(Input(Bool()))
  val c = IO(Output(Bool()))

  val tmp = a + b

  c := tmp
}

object Main extends App {
  (new ChiselStage).execute(
    Array("--target-dir", "build/", "--target", "systemverilog"),
    Seq(ChiselGeneratorAnnotation(() => new Basic))
  )
}
