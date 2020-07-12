-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class ru.citadel.rise.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class ru.citadel.rise.* { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class ru.citadel.rise.* { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}