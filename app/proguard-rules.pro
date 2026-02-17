# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ============================================================
# KOTLIN
# ============================================================
-keepattributes *Annotation*
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# ============================================================
# KOTLINX COROUTINES
# ============================================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ============================================================
# ANDROIDX & JETPACK COMPOSE
# ============================================================
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }
-dontwarn androidx.compose.**

# ============================================================
# ROOM DATABASE
# ============================================================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# Keep Room entity classes
-keep class com.michaelfrancoodev.studentcrud.data.local.entity.** { *; }

# Keep Room DAO classes
-keep class com.michaelfrancoodev.studentcrud.data.local.dao.** { *; }

# Keep Room database class
-keep class com.michaelfrancoodev.studentcrud.data.local.database.AppDatabase { *; }

# Keep all methods that return Flow or LiveData
-keepclassmembers class * {
    kotlinx.coroutines.flow.Flow *;
    androidx.lifecycle.LiveData *;
}

# ============================================================
# GSON (for JSON serialization)
# ============================================================
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep all model classes for Gson
-keep class com.michaelfrancoodev.studentcrud.domain.model.** { *; }

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ============================================================
# DATASTORE PREFERENCES
# ============================================================
-keep class androidx.datastore.*.** { *; }
-dontwarn androidx.datastore.**

# ============================================================
# WORK MANAGER
# ============================================================
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.InputMerger
-keep class androidx.work.impl.background.systemalarm.RescheduleReceiver { *; }

# ============================================================
# JAVA TIME (Desugared)
# ============================================================
-keep class j$.time.** { *; }
-dontwarn j$.time.**
-keep class java.time.** { *; }
-dontwarn java.time.**

# ============================================================
# GENERAL
# ============================================================
# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep Parcelable classes
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ============================================================
# PROJECT-SPECIFIC CLASSES
# ============================================================
# Keep all repository classes
-keep class com.michaelfrancoodev.studentcrud.data.repository.** { *; }

# Keep all ViewModel classes
-keep class com.michaelfrancoodev.studentcrud.ui.screens.**.viewmodel.** { *; }
-keep class **.*ViewModel { *; }

# Keep MainActivity
-keep class com.michaelfrancoodev.studentcrud.MainActivity { *; }
