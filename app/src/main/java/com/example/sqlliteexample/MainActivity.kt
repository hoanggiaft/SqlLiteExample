package com.example.sqlliteexample

import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayEmployeesOnList()

        btAdd.setOnClickListener {
            val id = editTextUserID.text.toString()
            val name = editTextUserName.text.toString()
            val email = editTextUserEmail.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (id.trim() != "" && name.trim() != "" && email.trim() != "") {
                val status =
                    databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(id), name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
                    editTextUserID.text.clear()
                    editTextUserName.text.clear()
                    editTextUserEmail.text.clear()
                    displayEmployeesOnList()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "id or name or email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        btDelete.setOnClickListener {
            //creating AlertDialog for taking user id
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.delete_dialog, null)
            dialogBuilder.setView(dialogView)

            val dltId = dialogView.findViewById(R.id.deleteId) as EditText
            dialogBuilder.setTitle("Delete Record")
            dialogBuilder.setMessage("Enter id below")
            dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

                val deleteId = dltId.text.toString()
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler= DatabaseHandler(this)
                if(deleteId.trim()!=""){
                    //calling the deleteEmployee method of DatabaseHandler class to delete record
                    val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"",""))
                    if(status > -1){
                        Toast.makeText(applicationContext,"record deleted",Toast.LENGTH_LONG).show()
                        displayEmployeesOnList()
                    }
                }else{
                    Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
                }

            })
            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                //pass
            })
            val b = dialogBuilder.create()
            b.show()
        }

        btUpdate.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.update_dialog, null)
            dialogBuilder.setView(dialogView)

            val edtId = dialogView.findViewById(R.id.updateId) as EditText
            val edtName = dialogView.findViewById(R.id.updateName) as EditText
            val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText

            dialogBuilder.setTitle("Update Record")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

                val updateId = edtId.text.toString()
                val updateName = edtName.text.toString()
                val updateEmail = edtEmail.text.toString()
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler= DatabaseHandler(this)
                if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!=""){
                    //calling the updateEmployee method of DatabaseHandler class to update record
                    val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail))
                    if(status > -1){
                        Toast.makeText(applicationContext,"Record update",Toast.LENGTH_LONG).show()
                        displayEmployeesOnList()
                    }
                }else{
                    Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
                }

            })
            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                //pass
            })
            val b = dialogBuilder.create()
            b.show()
        }



    }

   private fun displayEmployeesOnList()
    {
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        databaseHandler.getEmployees()

        val emp: List<EmpModelClass> = databaseHandler.getEmployees()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        var index = 0
        for(e in emp){
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            index++
        }
        //creating custom ArrayAdapter
        val myListAdapter = MyListAdapter(this,empArrayId,empArrayName,empArrayEmail)
        myListView.adapter = myListAdapter
    }
}
