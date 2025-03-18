package com.rey.barelauncher.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import com.rey.barelauncher.data.model.AppInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.rey.barelauncher.utils.AppUtils.isSystemApp

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("NewApi")
@Composable
fun AppItem(
    app: AppInfo,
    isFavorite: Boolean,
    onAppClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    isCompact: Boolean = false
) {
    var showOptions by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isSystem = remember(app.packageName) { isSystemApp(app.packageName, context) }

    Surface(
        modifier = Modifier
            .combinedClickable(
                onClick = onAppClick,
                onLongClick = { showOptions = true }
            )
            .then(
                if (isCompact)
                    Modifier.width(80.dp)
                else
                    Modifier.fillMaxWidth()
            ),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp
    ) {
        Box{

        }
        if (isCompact) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = app.icon.toBitmap().asImageBitmap(),
                        contentDescription = app.name,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    text = app.name,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    bitmap = app.icon.toBitmap().asImageBitmap(),
                    contentDescription = app.name,
                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = app.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                )

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        DropdownMenu(
            expanded = showOptions,
            onDismissRequest = { showOptions = false }
        ) {
            // App Info option is always available
            DropdownMenuItem(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${app.packageName}")
                    context.startActivity(intent)
                    showOptions = false
                },
                text = { Text("App Info") }
            )

            // For system apps, show Disable option instead of Uninstall
            if (isSystem) {
                DropdownMenuItem(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:${app.packageName}")
                        context.startActivity(intent)
                        Toast.makeText(context, "Navigate to Disable button", Toast.LENGTH_LONG).show()
                        showOptions = false
                    },
                    text = { Text("Disable") }
                )
            } else {
                // Regular uninstall for non-system apps
                DropdownMenuItem(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DELETE)
                        intent.data = Uri.parse("package:${app.packageName}")
                        context.startActivity(intent)
                        showOptions = false
                    },
                    text = { Text("Uninstall") }
                )
            }
        }
    }
}
