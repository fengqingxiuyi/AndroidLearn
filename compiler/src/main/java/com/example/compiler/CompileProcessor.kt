package com.example.compiler

import com.example.annotation.BindCompile
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.collections.HashSet


/**
 * @author fqxyi
 * @date 2020/8/14
 */
@AutoService(Processor::class)
class CompileProcessor : AbstractProcessor() {

    /**
     * 返回当前处理器能处理的注解集合
     */
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return Collections.singleton(BindCompile::class.java.canonicalName)
    }

    private var elementUtils: Elements? = null

    //自定义类要存放的文件位置
    private var filer: Filer? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        elementUtils = processingEnv?.elementUtils
        filer = processingEnv?.filer
    }

    //存储含有BindCompile注解的类的集合
    private var typeElementSet = HashSet<TypeElement>()

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val elements = roundEnv?.getElementsAnnotatedWith(BindCompile::class.java)
        //获取含有BindCompile注解的类的集合
        typeElementSet.clear()
        elements?.forEach { member ->
            val typeElement = member.enclosingElement as TypeElement
            typeElementSet.add(typeElement)
        }
        typeElementSet.iterator().forEach { typeElement ->
            //创建一个bindView的方法，参数为activity，并使用JvmStatic注解
            val bindFunBuilder = FunSpec.builder("bindView")
                .addParameter("activity", ClassName.bestGuess(typeElement.toString()))
                .addAnnotation(JvmStatic::class.java)
            //获取所有@BindCompile注解的属性
            elements?.forEach { member ->
                val bindCompile: BindCompile? = member.getAnnotation(BindCompile::class.java)
                //此处需要typeElement == member.enclosingElement判断，因为elements里包含了所有类中定义了BindCompile的注解
                if (bindCompile != null && typeElement == member.enclosingElement) {
                    //方法中添加findViewById
                    bindFunBuilder.addStatement("activity.${member.simpleName} = activity.findViewById(${bindCompile.value})")
                }
            }
            //生成一个由@BindCompileClass注解的类的名称加_bindView后缀的类，其中有一个静态方法bindView
            val fileSpec = FileSpec.builder(
                getPackageName(typeElement),
                typeElement.simpleName.toString() + "_bindView"
            )
                .addComment("Generated code from Butter Knife. Do not modify!")
                .addType(
                    TypeSpec.classBuilder(typeElement.simpleName.toString() + "_bindView")
                        .addType(
                            TypeSpec.companionObjectBuilder()
                                .addFunction(bindFunBuilder.build())
                                .build()
                        )
                        .build()
                )
                .build()
            filer?.apply {
                fileSpec.writeTo(this)
            }
        }
        return true
    }

    private fun getPackageName(type: TypeElement): String {
        return elementUtils?.getPackageOf(type)?.qualifiedName.toString()
    }

}