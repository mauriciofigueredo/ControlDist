//package com.mst.controldist.views
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ExitToApp
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth
//import com.mst.controldist.viewModels.CamViewModel
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeView(navController: NavController, camViewModel: CamViewModel){
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {Text("Caminantes")},
//                navigationIcon = {
//                    IconButton(onClick = {
//                        camViewModel.signOut()
//                        navController.popBackStack()
//                    }) {
//                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "")
//                    }
//                }
//            )
//        }
//    ) { pad ->
//        Column(
//            modifier = Modifier.padding(pad),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Home", fontSize = 30.sp, fontWeight = FontWeight.Bold)
//        }
//    }
//
//
//
//}

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.mst.controldist.viewModels.CamViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeView(navController: NavController, camViewModel: CamViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    var photoName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasCameraPermission) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Se requiere permiso de cámara")
            Button(onClick = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text("Solicitar Permiso")
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .size(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("📷", style = MaterialTheme.typography.headlineLarge)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nombre de la foto") },
            text = {
                OutlinedTextField(
                    value = photoName,
                    onValueChange = { photoName = it },
                    label = { Text("Ingresa el nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (photoName.isNotBlank()) {
                            capturePhoto(context, imageCapture, photoName.trim())
                            showDialog = false
                            photoName = ""
                        } else {
                            Toast.makeText(
                                context,
                                "Ingresa un nombre válido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Capturar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    photoName = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture?,
    customName: String
) {
    val imgCapture = imageCapture ?: return

    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        .format(System.currentTimeMillis())
    val name = "${customName}_$timestamp"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MiApp")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imgCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Toast.makeText(
                    context,
                    "Foto guardada como: $name",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(
                    context,
                    "Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}