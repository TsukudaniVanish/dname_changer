package usecase

trait DriveHandler {
    def GetMaxPageSize(): Int 
    /**
      * 
      *
      * @param pageSize
      * @param nextPageToken
      * @return files, nextPageToken
      */
    def FindFiles(pageSize: Int, nextPageToken:String): (Seq[File], Option[String])
}