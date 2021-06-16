package com.mgk.unscramblegame

import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mgk.unscramblegame.databinding.GameFragmentBinding
import kotlinx.android.synthetic.main.game_fragment.*

class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()

    private lateinit var binding: GameFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameFragmentBinding.inflate(inflater, container, false)
        viewModel.currentScrambledWord.observe(viewLifecycleOwner,{newWord->
            binding.textViewUnscrambledWord.text=newWord
        })
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateUI()
        binding.skip.setOnClickListener { onSkip() }
        binding.submit.setOnClickListener { onSubmit() }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showFinalScoreDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    private fun onSkip() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateUI()
        } else {
            showFinalScoreDialog()
        }
    }

    private fun onSubmit() {

        val answer = binding.textInputEditText.text.toString()
        if(viewModel.isCorrectAnswer(answer)){
            setErrorTextField(false)
            if(viewModel.nextWord()){
                updateUI()
            }
            else{
                showFinalScoreDialog()
            }
        }
        else{setErrorTextField(true)}

    }
    fun updateUI(){
        binding.wordCount.text = getString(R.string.word_count, viewModel.currentWordCount, MAX_NO_OF_WORDS)
        binding.score.text = getString(R.string.score, viewModel.score)
    }
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateUI()
    }

    private fun exitGame(){
        activity?.finish()
    }
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
}