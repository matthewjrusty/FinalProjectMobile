/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding
import timber.log.Timber

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Pulau Sumatera sebelah selatan dan barat berbatasan dengan?",
                    answers = listOf("Selat Sunda dan Samudera Hindia",
                            "Selat Sunda dan Samudera Indonesia",
                            "Selat Sunda dan Samudera Pasifik",
                            "Selat Sunda dan Samudera Arktik")),
            Question(text = "Sebelah Timur Pulau Sumatera berbatasan dengan?",
                    answers = listOf("Selat Malaka",
                            "Samudera Hindia",
                            "Teluk Benggala",
                            "Selat Sunda")),
            Question(text = "Perhatikan provinsi berikut!\n" +
                    "(1) Jambi\n" +
                    "(2) Riau\n" +
                    "(3) Gorontalo\n" +
                    "(4) Bengkulu\n" +
                    "(5) Lampung\n" +
                    "Provinsi yang ada di Pulau Sumatera ditunjukkan oleh nomor?",
                    answers = listOf("(1), (2), (4), dan (5)", "(1), (2), (3), dan (4)", "(1), (2), (3), dan (5)", "(1), (3), (4), dan (5)")),
            Question(text = "Di wilayah Indonesia terdapat banyak gunung api yang masih aktif. salah satu contoh gunung api aktif di Indonesia yaitu Gunung Merapi. Gunung tersebut terletak di wilayah provinsi?",
                    answers = listOf("Jawa Tengah dan Daerah Istimewa Yogyakarta", "DKI Jakarta dan Banten", "Jawa Barat dan DKI Jakarta", "Jawa Timur dan Jawa Tengah")),
            Question(text = "Candi Borobudur merupakan kenampakan buatan yang merupakan ikon dari provinsi?",
                    answers = listOf("Jawa Tengah", "Daerah Istimewa Yogyakarta", "DKI Jakarta", "Jawa Barat")),
            Question(text = "Banjir di ibu kota sudah menjadi hal yang biasa, hal ini dikarenakan sungai, dan penampungan air sudah terpenuhi oleh sampah. Hal yang dapat dilakukan untuk mencegah hal tersebut adalah?",
                    answers = listOf("Membuang sampah pada tempatnya", "Melestarikan hutan mangrove", "Penanaman hutan kembali", "Mematikan keran air jika tidak digunakan")),
            Question(text = "Fungsi dari pembuluh nadi adalah?",
                    answers = listOf("Transportasi oksigen", "Karbondioksida", "Air", "Sari-sari makanan")),
            Question(text = "Di bawah ini yang bukan termasuk makhluk hidup yaitu?",
                    answers = listOf("Air", "Manusia", "Hewan", "Tumbuhan")),
            Question(text = "Manusia melakukan perkembangbiakan dengan cara?",
                    answers = listOf("Melahirkan", "Bertelur", "Bertunas", "Melahirkan dan bertelur")),
            Question(text = "Berikut ini hewan yang melakukan perkembangbiakan dengan bertelur yaitu?",
                    answers = listOf("Angsa, itik dan bebek", "Angsa, bebek dan kelinci", "Hiu, ayam dan cicak", "Kera, bebek dan ayam")),
            Question(text = "Flora dan Fauna dapat dikatakan mengalami kepunahan apabila jumlahnya sudah?",
                    answers = listOf("Berkurang", "Bertambah", "Melimpah", "Banyak")),
            Question(text = "Buah yang secara terus-menerus dibiarkan ditempat terbuka maka akan menyebabkan buah menjadi?",
                   answers = listOf("Busuk", "Harum", "Besar", "Banyak")),
            Question(text = "Sebuah pentagon memiliki berapa sisi?",
                    answers = listOf("5", "4", "6", "7")),
            Question(text = "Pilihlah jawaban yang tepat untuk mengisi bagian yang kosong: 18 + _ = 25?",
                    answers = listOf("7", "8", "6", "3")),
            Question(text = "Apa jumlah dua kali lipat dari 40?",
                    answers = listOf("80", "8", "16", "40")),
            Question(text = "Michelle mempunyai 12 mobil mainan. Michelle kehilangan 4 mobil mainan. Berapakah sisa mobil mainan Michelle yang ada?",
                    answers = listOf("8", "16", "10", "3")),
            Question(text = "Sebuah piring nasi goreng berharga Rp. 15,000. Berapakah harga dari 3 piring nasi goreng?",
                    answers = listOf("Rp. 45,000", "Rp. 30,000", "Rp. 25,000", "Rp. 35,000")),
            Question(text = "Ariana memiliki 15 buah permen yang dibagikan rata ke 3 temannya. Berapa jumlah permen yang didapatkan masing-masing teman?",
                    answers = listOf("5", "3", "4", "6"))
            
    )



    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 18

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // setQuestion()
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController()
                                .navigate(GameFragmentDirections
                                        .actionGameFragmentToGameWonFragment(numQuestions, questionIndex))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController()
                            .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }
            }
        }
        return binding.root
    }

     // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
