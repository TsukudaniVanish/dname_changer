import usecase.File
import domain.LSInput
import domain.UpdateFileInput
class RepositoryMock extends usecase.Repository {
    def FindSegments(lsInput: LSInput): (Seq[File], String) = {
        (List(), "")
    } 

    def UpdateFile(updateFileInput: UpdateFileInput): Either[String, File] = {
        if updateFileInput.name == "ok" then {
            Right(FileMock())
        } else {
            Left("this is err")
        }
    }
}

class FileMock extends usecase.File {
    def getName(): String = "ok"
    def getID(): String = "1"
}