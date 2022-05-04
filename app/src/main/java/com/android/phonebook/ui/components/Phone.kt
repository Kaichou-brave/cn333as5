package com.android.phonebook.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.phonebook.domain.model.PhoneModel
import com.android.phonebook.util.fromHex

@ExperimentalMaterialApi
@Composable
fun Phone(
    modifier: Modifier = Modifier,
    phone: PhoneModel,
    onPhoneClick: (PhoneModel) -> Unit = {},
    onPhoneCheckedChange: (PhoneModel) -> Unit = {},
    isSelected: Boolean
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = {
                if (phone.middle_name == "") {
                    Text(
                        text = phone.first_name + " " + phone.last_name + " (" + phone.tag + ")",
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = phone.first_name + " " + phone.middle_name + " " + phone.last_name + " (" + phone.tag + ")",
                        maxLines = 1
                    )
                }
            },
            secondaryText = {
                if (phone.contact.length == 10) {
                    Text(
                        text = "" + phone.contact.subSequence(
                            0,
                            3
                        ) + "-" + phone.contact.subSequence(3, 6) + "-" + phone.contact.subSequence(
                            6,
                            9
                        ) + phone.contact[9], maxLines = 1
                    )
                } else if (phone.contact.length == 9) {
                    Text(
                        text = "" + phone.contact.subSequence(
                            0,
                            3
                        ) + "-" + phone.contact.subSequence(3, 6) + "-" + phone.contact.subSequence(
                            6,
                            8
                        ) + phone.contact[8], maxLines = 1
                    )
                } else {
                    Text(
                        text = phone.contact, maxLines = 1
                    )
                }
            },
            icon = {
                PhoneColor(
                    color = Color.fromHex(phone.color.hex),
                    size = 40.dp,
                    border = 1.dp
                )
            },
            trailing = {
                if (phone.isCheckedOff != null) {
                    Checkbox(
                        checked = phone.isCheckedOff,
                        onCheckedChange = { isChecked ->
                            val newPhone = phone.copy(isCheckedOff = isChecked)
                            onPhoneCheckedChange.invoke(newPhone)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            },
            modifier = Modifier.clickable {
                onPhoneClick.invoke(phone)
            }
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PhonePreview() {
    Phone(
        phone = PhoneModel(1, "first", "", "last", "0000000000", "Mobile", null),
        isSelected = false
    )
}