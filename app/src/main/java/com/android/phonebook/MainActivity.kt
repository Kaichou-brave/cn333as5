package com.android.phonebook

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.phonebook.screen.SavePhoneScreen
import com.android.phonebook.routing.PhoneBookRouter
import com.android.phonebook.routing.Screen
import com.android.phonebook.screen.PhoneScreen
import com.android.phonebook.screen.TrashScreen
import com.android.phonebook.ui.theme.PhoneBookTheme
import com.android.phonebook.viewmodel.MainViewModel
import com.android.phonebook.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneBookTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                MainActivityScreen(viewModel)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MainActivityScreen(viewModel: MainViewModel) {
    Surface {
        when (PhoneBookRouter.currentScreen) {
            is Screen.Phones -> PhoneScreen(viewModel)
            is Screen.SavePhone -> SavePhoneScreen(viewModel)
            is Screen.Trash -> TrashScreen(viewModel)
        }
    }
}

