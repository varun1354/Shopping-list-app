package com.example.shoppingapp

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


data class ShoppingItems(
    val id:Int,
    var name:String,
    var quantity:Int,
    var isEditing:Boolean = false

)

@Composable

fun shoppinglist()
{

        var sItems by remember { mutableStateOf(listOf<ShoppingItems>()) }
        var showDialog by remember { mutableStateOf(false) }
        var itemName by remember { mutableStateOf("") }
        var itemQuantity by remember { mutableStateOf("")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ){
            Button(onClick = {showDialog=true}, modifier = Modifier.align(Alignment.CenterHorizontally)) {

                Text("Add Item")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp))
            {
                items(sItems){
                    item ->
                    if (item.isEditing)
                    {
                        ShoppingItemsEditor(item = item, OnEditDone = {
                            EditedName , editedQuantity ->
                            sItems = sItems.map{it.copy(isEditing = false)}
                            val editedItem =sItems.find{it.id == item.id}
                            editedItem?.let { it.name = EditedName
                            it.quantity = editedQuantity
                            }
                        })}
                    else
                    {
                        ShoppingItemsList(item = item, OnEditClick = {
                            sItems = sItems.map{it.copy(isEditing = it.id==item.id)}
                        }, ToDelete = {sItems =sItems-item
                        })
                    }
                }
            }
        }

    if(showDialog)
    {
        AlertDialog(onDismissRequest = { showDialog=false },
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                Button(onClick = {
                                    if(itemName.isNotEmpty())
                                    {
                                        val newitem = ShoppingItems(
                                            id = sItems.size+1,
                                            name = itemName,
                                            quantity = itemQuantity.toInt()
                                        )
                                        sItems = sItems+newitem
                                        itemName =""
                                        showDialog = false
                                        itemQuantity =""
                                    }

                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = { showDialog = false }) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text("Add Shopping Items")},
            text = {
                Column {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))

                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                }
            }
        )
            
        }
    }

@Composable
fun ShoppingItemsEditor(item: ShoppingItems , OnEditDone: (String,Int) -> Unit)
{
    var EditedName by remember { mutableStateOf(item.name) }
    var editedQuantity  by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp), horizontalArrangement =Arrangement.SpaceEvenly){
        Column {
            BasicTextField(value = EditedName,
                onValueChange = {EditedName=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)) 
        }
        Button(onClick = { isEditing = false
        OnEditDone(EditedName,editedQuantity.toIntOrNull()?:1)
        }) {

        }
    }
}

@Composable

fun ShoppingItemsList(
    item : ShoppingItems,
    OnEditClick : () -> Unit,
    ToDelete: () -> Unit
)
{
        Row( modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(20)
            ), horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(text = item.name, modifier = Modifier.padding(8.dp))
            Text("Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
            Row (modifier = Modifier.padding(8.dp)){
                IconButton(onClick = OnEditClick){
                    Icon(imageVector = Icons.Default.Edit , contentDescription = null )
                    
                }
                IconButton(onClick = ToDelete){
                    Icon(imageVector = Icons.Default.Delete , contentDescription = null )
                }

            }
        }
}


