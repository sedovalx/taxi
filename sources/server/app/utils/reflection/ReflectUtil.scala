package utils.reflection

object ReflectUtil {
  import reflect.runtime.universe._

  def getMembers[T](implicit tt: TypeTag[T]) = typeOf[T].members.view

  def getFields[T](implicit tt: TypeTag[T]) = getMembers[T].filter { !_.isMethod }

  def getOnlyColumnFields[T](implicit tt: TypeTag[T]) =
    getFields[T].filter { _.typeSignature.baseClasses.count { c => c.name.toString == "Column" } > 0 }

  def getColumnFieldsMap[T](implicit tt: TypeTag[T]) =
    getOnlyColumnFields[T]
      .map{i => i.name.toString -> (i.typeSignature match { case TypeRef(_, _, args) => args }).head}
      .toMap
}
