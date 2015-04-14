package t

class EntityNotFoundException[T](message: String)(implicit val ev: Manifest[T])
  extends RuntimeException(s"Entity of type ${ev.runtimeClass.getTypeName} not found: $message")
