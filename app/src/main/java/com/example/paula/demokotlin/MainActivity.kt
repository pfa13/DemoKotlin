package com.example.paula.demokotlin

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), RecognitionListener {

    var toSpeech: TextToSpeech? = null
    var result: Int? = null
    var text: String = ""
    lateinit var speech: SpeechRecognizer
    lateinit var intentReco: Intent
    var LOG_TAG: String = "VoiceRecognitionActivity"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    override fun onStart() {
        super.onStart()
        toSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {
            result = toSpeech?.setLanguage(Locale("es", "ES"))
            text = "Bienvenido. ¿Qué quieres hacer?. ¿Leer?. ¿Enviar?"
            toSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null)

            speech = SpeechRecognizer.createSpeechRecognizer(this)
            speech.setRecognitionListener(this)
            intentReco = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intentReco.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es")
            intentReco.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.packageName)
            intentReco.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
            intentReco.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            Thread.sleep(6000)
            while(true) {
                if (!toSpeech!!.isSpeaking) {
                    Log.i(LOG_TAG, "he terminado de hablar")
                    toSpeech?.stop()
                    toSpeech?.shutdown()
                    break
                }
            }
            speech.startListening(intentReco)
        })
    }

    override fun onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech")
    }

    override fun onBufferReceived(p0: ByteArray?) {
        Log.i(LOG_TAG, "onBufferReceived")
    }

    override fun onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech")
    }

    override fun onError(p0: Int) {
        Log.i(LOG_TAG, "on error")
        var errorMensaje: String = getErrorText(p0)
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        Log.i(LOG_TAG, "onEvent")
    }

    override fun onResults(p0: Bundle?) {
        Log.i(LOG_TAG, "onResults")
        var matches: ArrayList<String> = p0!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text: String = ""
        for (result in matches) {
            Log.i(LOG_TAG, result)
            text += result + "\n"
            if (result.contentEquals("leer") || result.contentEquals("Leer") || result.contentEquals("LEER")) {
                Log.i(LOG_TAG, "Abro activity leer")
                val intent: Intent = Intent(this, Leer::class.java)
                startActivity(intent)
                break
            } else {
                textView.text = "No has dicho leer"
                Log.i(LOG_TAG, "No has dicho leer")
                break
            }
        }
    }

    override fun onPartialResults(p0: Bundle?) {
        Log.i(LOG_TAG, "onPartialResults")
    }

    override fun onRmsChanged(p0: Float) {
        Log.i(LOG_TAG, "onRmsChanged" + p0)
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.i(LOG_TAG, "onReadyForSpeech")
    }

    fun getErrorText(errorCode: Int): String {
        val message: String = ""
        when (errorCode) {
        /*SpeechRecognizer.ERROR_AUDIO -> message = "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> message = "Client side error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "Insufficient permissions"
        SpeechRecognizer.ERROR_NETWORK -> message = "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> message = "No match"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RecognitionService busy"
        SpeechRecognizer.ERROR_SERVER -> message = "error from server"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "No speech input"
        else -> message = "Didn't understand, please try again."*/
            SpeechRecognizer.ERROR_AUDIO -> Log.i(LOG_TAG, "Audio recording error")
            SpeechRecognizer.ERROR_CLIENT -> Log.i(LOG_TAG, "client side error")
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Log.i(LOG_TAG, "insufficient permissions")
            SpeechRecognizer.ERROR_NETWORK -> Log.i(LOG_TAG, "network error")
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.i(LOG_TAG, "network timeout")
            SpeechRecognizer.ERROR_NO_MATCH -> Log.i(LOG_TAG, "no match")
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.i(LOG_TAG, "recognitionservice busy")
            SpeechRecognizer.ERROR_SERVER -> Log.i(LOG_TAG, "error from server")
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.i(LOG_TAG, "no speech input")
            else -> Log.i(LOG_TAG, "didn't undestand, please try againa")
        }
        return message
    }

    /*fun TTS(view: View) {
        when (view.id) {
            R.id.button ->
                if(result === TextToSpeech.LANG_MISSING_DATA || result === TextToSpeech.LANG_NOT_SUPPORTED)
                    Toast.makeText(applicationContext, "Feature not supported", Toast.LENGTH_SHORT).show()
                else {
                    var texto = findViewById<EditText>(R.id.editText)
                    text = "Hello world"
                    toSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
                }
            R.id.button2 ->
                if (toSpeech === null)
                    toSpeech?.stop()

        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        if(toSpeech !== null) {
            Log.i(LOG_TAG, "he destruido")
            toSpeech?.stop()
            toSpeech?.shutdown()
        }
    }

    internal inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val _swipeThresold = 100
        private val _swipeVelocityThresold = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(event1: MotionEvent, event2: MotionEvent,
                             velocityX: Float, velocityY: Float): Boolean {

            var result = false
            try {
                val diffY = event2.y - event1.y
                val diffX = event2.x - event1.x

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > _swipeThresold && Math.abs(velocityX) > _swipeVelocityThresold) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                } else if (Math.abs(diffY) > _swipeThresold && Math.abs(velocityY) > _swipeVelocityThresold) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                }
                result = true

            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }

        private fun onSwipeRight() {
            Toast.makeText(baseContext,
                    "Swipe right - startActivity()",
                    Toast.LENGTH_SHORT).show()

            //switch another activity
            val intent = Intent(
                    this@MainActivity, Conversacion::class.java)
            startActivity(intent)
        }

        private fun onSwipeLeft() {
            Toast.makeText(baseContext,
                    "Swipe left - startActivity()",
                    Toast.LENGTH_SHORT).show()

            //switch another activity
            val intent = Intent(
                    this@MainActivity, Leer::class.java)
            startActivity(intent)
        }

        private fun onSwipeTop() {
            Toast.makeText(baseContext,
                    "Swipe up - startActivity()",
                    Toast.LENGTH_SHORT).show()

            //switch another activity
            val intent = Intent(
                    this@MainActivity, Leer::class.java)
            startActivity(intent)
        }

        private fun onSwipeBottom() {
            Toast.makeText(baseContext,
                    "Swipe down - startActivity()",
                    Toast.LENGTH_SHORT).show()

            //switch another activity
            val intent = Intent(
                    this@MainActivity, Leer::class.java)
            startActivity(intent)
        }
    }
}
