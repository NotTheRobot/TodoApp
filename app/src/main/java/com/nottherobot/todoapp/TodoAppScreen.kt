package com.nottherobot.todoapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nottherobot.todoapp.ui.EditTodoScreen
import com.nottherobot.todoapp.ui.TodoListScreen

interface NavigationDestination{
    val route: String
}

@Composable
fun TodoAppScreen(navHostController: NavHostController = rememberNavController()){
    NavHost(
        navController = navHostController,
        startDestination = TodoListScreen.route
    ){
        composable(route = TodoListScreen.route) {
            TodoListScreen(
                navigateToEditTodo = {
                    if(it != null){
                        navHostController.navigate("${EditTodoScreen.route}/$it")
                    }else{
                        navHostController.navigate(EditTodoScreen.route)
                    }
                })
        }

        composable(route = EditTodoScreen.routeWithArgs){
            EditTodoScreen(
                onNavigateUp = { navHostController.navigateUp() }
            )
        }

        composable(route = EditTodoScreen.route){
            EditTodoScreen(
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
    }
}
