package org.specs2
package main

import control.Exceptions._

private[specs2]
trait ArgumentsArgs {
  /** shorthand method to create an Arguments object */
  def args(  
     ex: String                = ".*",
     xonly: Boolean            = Arguments().xonly,
     failtrace: Boolean        = Arguments().failtrace,
     noindent: Boolean         = Arguments().noindent,
     specName: String          = Arguments().specName,
     sequential: Boolean       = Arguments().sequential,
     threadsNb: Int            = Arguments().threadsNb
    
  ) = Arguments(".*"+ex+".*", xonly, failtrace, noindent, specName, sequential, threadsNb)
  
  /** 
   * @return arguments for a literate specification: no auto indent and a sequential
   *         execution
   */
  def literate = args(noindent = true, sequential = true)  
}
private[specs2]
object ArgumentsArgs extends ArgumentsArgs

private[specs2]  
case class Arguments (
  ex: String                 = ".*",
  xonly: Boolean            = false,
  failtrace: Boolean        = false,
  noindent: Boolean         = false,
  specName: String          = ".*Spec",
  sequential: Boolean       = false,
  threadsNb: Int            = 4
)

private[specs2]  
case object Arguments {
  
  /** @return new arguments from command-line arguments */
  def apply(implicit arguments: String*): Arguments = {
    extract(new Arguments())(arguments)
  }
  /** 
   * @return new arguments from command-line arguments, with default values which
   *         may be different from the ones in the Arguments class 
   */
  def apply(defaults: Arguments)(implicit arguments: String*): Arguments = {
    extract(defaults)(arguments)
  }
  private def extract(defaults: Arguments)(implicit arguments: Seq[String]): Arguments = {
    new Arguments (
       xonly = bool("xonly", defaults.xonly),
       failtrace = bool("failtrace", defaults.failtrace),
       specName = value("specName", defaults.specName),
       sequential = bool("sequential", defaults.sequential),
       threadsNb = int("threadsNb", defaults.threadsNb)
    )
  }
  
  private def bool(name: String, defaultValue: Boolean)(implicit args: Seq[String]) = {
    args.find(_.toLowerCase.contains(name.toLowerCase)).map(a => true).getOrElse(defaultValue)
  }
  private def value(name: String, defaultValue: String)(implicit args: Seq[String]) = {
    args.zip(args.drop(1)).find(_._1.toLowerCase.contains(name.toLowerCase)).map(_._2).getOrElse(defaultValue)
  }
  private def int(name: String, defaultValue: Int)(implicit args: Seq[String]) = {
    tryOrElse(value(name, "")(args).toInt)(defaultValue)
  }
}
