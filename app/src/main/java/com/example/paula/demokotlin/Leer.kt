package com.example.paula.demokotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_leer.*
import java.util.*

class Leer : AppCompatActivity(), RecognitionListener {

    var toSpeech: TextToSpeech? = null
    var result: Int? = null
    var text: String = ""
    lateinit var speech: SpeechRecognizer
    lateinit var intentReco: Intent
    var LOG_TAG: String = "VoiceRecognitionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leer)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    override fun onStart() {
        super.onStart()
        toSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {
            result = toSpeech?.setLanguage(Locale("es", "ES"))
            text = "Tienes un mensaje nuevo de Paula. ¿Quieres leerlo?"
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
                    Log.i(LOG_TAG, "finish")
                    toSpeech?.stop()
                    toSpeech?.shutdown()
                    break
                }
            }
            speech.startListening(intentReco)
        })
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.i(LOG_TAG, "onReadyForSpeech")
    }

    override fun onRmsChanged(p0: Float) {
        Log.i(LOG_TAG, "onRmsChanged" + p0)
    }

    override fun onBufferReceived(p0: ByteArray?) {
        Log.i(LOG_TAG, "onBufferReceived")
    }

    override fun onPartialResults(p0: Bundle?) {
        Log.i(LOG_TAG, "onPartialResults")
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        Log.i(LOG_TAG, "onEvent")
    }

    override fun onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech")
    }

    override fun onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech")
    }

    override fun onError(p0: Int) {
        Log.i(LOG_TAG, "on error")
        var errorMensaje: String = getErrorText(p0)
    }

    override fun onResults(p0: Bundle?) {
        Log.i(LOG_TAG, "onResults")
        var matches: ArrayList<String> = p0!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text: String = ""
        for (result in matches) {
            textView2.text = text
            text += result + "\n"
            if (result.contentEquals("si") || result.contentEquals("sí") || result.contentEquals("Si") || result.contentEquals("SI")) {
                val intent: Intent = Intent(this, Conversacion::class.java)
                startActivity(intent)
                break
            } else {

            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        if(toSpeech !== null) {
            Log.i(LOG_TAG, "me destruyo")
            toSpeech?.stop()
            toSpeech?.shutdown()
        }
    }
}

