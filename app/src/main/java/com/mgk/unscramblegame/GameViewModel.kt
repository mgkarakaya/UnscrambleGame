package com.mgk.unscramblegame

import androidx.lifecycle.ViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameViewModel :ViewModel() {
        private var _score = 0
        private var _currentWordCount = 0
        private val _currentScrambledWord=MutableLiveData<String>()
        private lateinit var currentWord:String
        private var mutableList= mutableListOf<String>()


        val score:Int get() = _score
        val currentWordCount:Int get() = _currentWordCount

        val currentScrambledWord:LiveData<String> get() = _currentScrambledWord
init {
    getNextWord()
}
        private fun getNextWord() {
                currentWord = listOfWords.random()
                val tempWord=currentWord.toCharArray()
                tempWord.shuffle()

                while (tempWord.toString().equals(currentWord, false)) {
                        tempWord.shuffle()
                }
                if(mutableList.contains(currentWord))
                {getNextWord() }
                else{
                        _currentScrambledWord.value= String(tempWord)
                        ++_currentWordCount
                }

        }
        fun nextWord():Boolean{
                return if(currentWordCount< MAX_NO_OF_WORDS) {
                        getNextWord()
                        true
                } else false
        }
        private fun increaseScore(){
                _score += SCORE_INCREASE
        }
         fun isCorrectAnswer(answer:String):Boolean{
                if (answer.trim().equals(currentWord)) {
                        increaseScore()
                        return true
                }
                return false
        }
        fun reinitializeData() {
                _score = 0
                _currentWordCount = 0
                mutableList.clear()
                getNextWord()
        }

}