package usecase

import domain.Command

trait Parser {
    def getUseage(): String
    def getArg(): Seq[String]
    def ParseArg(args: Seq[String]): Command
}
