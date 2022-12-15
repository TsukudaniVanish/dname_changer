package usecase

trait Repository {
    /**
      * 
      *
      * @param driveHandler
      * @param pageMax
      * @return files, nextPageToken
      */
    def FindFiles(driveHandler: DriveHandler, pageMax: Int): (Seq[File], Option[String])
}