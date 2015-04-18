package t

import scala.reflect.ClassTag

class EntityNotFoundException[T](message: String, entity : String)
  extends RuntimeException(s"Entity of type ${entity} not found: $message")
