package com.example.annotation

/**
 * @author fqxyi
 * @date 2020/8/14
 */
object Binding {

    /**
     * 编译时注解
     */
    fun bindCompile(target: Any) {
        try {
            val classs = target.javaClass
            val claName = classs.name + "_bindView"
            val clazz = Class.forName(claName)

            val bindMethod = clazz.getMethod("bindView", target::class.java)
            val ob = clazz.newInstance()
            bindMethod.invoke(ob, target)
        } catch (e : Exception) {
            println("Binding Compile => " +  e.message)
        }
    }

}