package usecase

trait Repository {
    /**
      * 
      *
      * @return files, nextPageToken
      */
    def FindSegments(
        lsInput: domain.LSInput,
    ): (Seq[File], String)

    def UpdateFile(
        updateFileInput: domain.UpdateFileInput
  ): Either[String, File]
}