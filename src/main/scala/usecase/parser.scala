package usecase

import domain.Command

trait Parser {
    def getArg(): Seq[String]
    def ParseArg(args: Seq[String]): Command
}