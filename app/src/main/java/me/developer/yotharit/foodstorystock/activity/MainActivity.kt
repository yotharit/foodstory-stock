package me.developer.yotharit.foodstorystock.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import me.developer.yotharit.foodstorystock.R
import me.developer.yotharit.foodstorystock.model.Product
import me.developer.yotharit.foodstorystock.model.TransactionLog
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var btnProduct: Button
    lateinit var btnLog: Button
    lateinit var tvData: TextView

    lateinit var firebaseDb: FirebaseFirestore

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFireBase()
        initInstance()
    }

    fun initInstance() {
        btnProduct = findViewById(R.id.btnProduct)
        btnLog = findViewById(R.id.btnLog)
        tvData = findViewById(R.id.tvData)

        btnProduct.setOnClickListener {
            Log.d("BTN CLIKCED", "Clicked")
            addRandomData()
        }

        btnLog.setOnClickListener {
            Log.d("BTN CLIKCED", "Clicked")
            var log : TransactionLog = TransactionLog()
            log.customer = "b"
            log.date = Calendar.getInstance().time
            log.description = "sold"
            log.user = "c"
            addLogTransactionToDb("1",log)
        }
    }

    fun initFireBase() {
        firebaseDb = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        firebaseDb.setFirestoreSettings(settings)
    }

    fun addRandomData() {
        var product: Product = Product()
        product.serialId = "1"
        product.incomeDate = Calendar.getInstance().time
        product.productId = "22222"
        product.productStatus = "in stock"
        product.manufacturer = "some one"

        var log: TransactionLog = TransactionLog()
        log.customer = "a"
        log.date = null
        log.description = "Receive"
        log.user = "userA"

        addProductToDb(product, log)
    }

    fun addProductToDb(product: Product, log: TransactionLog) {

        var productRef: DocumentReference = firebaseDb.collection("products").document(product.serialId!!)
        var logRef: DocumentReference = productRef.collection("logs").document()

        try {
            firebaseDb.runTransaction { transaction ->
                transaction.set(productRef, product)
                transaction.set(logRef, log)
                null
            }.addOnCompleteListener {
                Log.i("Add Transaction", "complete")
            }.addOnFailureListener {
                Log.i("Add Transaction", "fail")
                //handle
            }.addOnSuccessListener {
                Log.i("Add Transaction", "success")
            }.addOnCanceledListener {
                Log.i("Add Transaction", "cancel")
            }
        } catch (e : Exception){
            tvData.text = "Exception " + e.toString()
        }

    }

    fun addLogTransactionToDb(serialId: String, log: TransactionLog) {
        var productRef = firebaseDb.collection("products").document(serialId)
        var logRef = productRef.collection("logs").document()

        var product : Product? = null

        try {
            productRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                Log.d("Something","in")
                if (task.isSuccessful) {
                    val document = task.result
                    if(document.exists())
                        product = document.toObject(Product::class.java)
                    if(product != null) {
                        product!!.productStatus = log.description
                        firebaseDb.runTransaction { transaction ->
                            transaction.set(productRef, product!!)
                            transaction.set(logRef, log)
                            null
                        }.addOnCompleteListener {
                            Log.i("Add Transaction", "complete")
                        }.addOnFailureListener {
                            Log.i("Add Transaction", "fail")
                            //handle
                        }.addOnSuccessListener {
                            Log.i("Add Transaction", "success")
                        }.addOnCanceledListener {
                            Log.i("Add Transaction", "cancel")
                        }
                    }
                } else {
                    Log.d("Something","fail")
                }
            })
        }catch (e : Exception){
            tvData.text = "Exception " + e.toString()
        }




    }


}
