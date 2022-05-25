package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saboon.timetable.Adapters.DetailsRecyclerAdapter
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.R
import com.saboon.timetable.Utils.IDGenerator
import com.saboon.timetable.ViewModels.DetailsViewModel
import com.saboon.timetable.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding?=null
    private val binding get() =_binding!!


    lateinit var viewModel: DetailsViewModel
    private val recyclerAdapter = DetailsRecyclerAdapter(arrayListOf())

    private lateinit var LESSON: ModelLesson


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //eger geri tusuna basilirsa(telefondan) burasi calisir
//        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                //onBackPressed()
//                val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
//                findNavController().navigate(actionToBack)
//            }
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false)

        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)


        arguments?.let {
            if(it != null){
                DetailsFragmentArgs.fromBundle(it).selectedLessonID?.let {
                    val lessonID = it
                    viewModel.getDataFromSQLite(lessonID)
                }

            }
        }

        binding.fragmentDetailsRecyclerViewProgramRecycler.layoutManager = LinearLayoutManager(context)

        binding.fragmentDetailsRecyclerViewProgramRecycler.adapter = recyclerAdapter


        observeLiveData()

        showValuesInUI()

        buttons()
    }


    fun observeLiveData(){
        viewModel.lesson.observe(viewLifecycleOwner, Observer {
            if (it != null){
                LESSON = it
            }
        })
        viewModel.programTimes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.VISIBLE
                recyclerAdapter.updateList(it)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.GONE
                binding.detailsLoadingProgressBar.visibility = View.VISIBLE
                binding.detailsErrorText.visibility = View.GONE
                binding.detailsEmptyText.visibility = View.GONE
            }else{
                binding.detailsLoadingProgressBar.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.GONE
                    binding.detailsLoadingProgressBar.visibility = View.GONE
                    binding.detailsErrorText.visibility = View.VISIBLE
                    binding.detailsEmptyText.visibility = View.GONE
                }else{
                    binding.detailsErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.empty.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.GONE
                    binding.detailsLoadingProgressBar.visibility = View.GONE
                    binding.detailsErrorText.visibility = View.GONE
                    binding.detailsEmptyText.visibility = View.VISIBLE
                }else{
                    binding.detailsEmptyText.visibility = View.GONE
                }
            }
        })


    }

    fun showValuesInUI(){
        binding.fragmentDetailsTextViewLessonName.text = LESSON.lessonName
        binding.fragmentDetailsTextViewLecturerName.text = LESSON.lecturerName
        binding.framentDetailsTextViewAbsenteeism.text = LESSON.absenteeism
    }


    fun buttons(){
        binding.fragmentDetailsTextViewLessonDetails.setOnClickListener {
            //onBackPressed()
            val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
            findNavController().navigate(actionToBack)
        }

        binding.fragmentDetailsTextViewAddTime.setOnClickListener {

            if (LESSON.id != "null"){
                val actionToAddProgram = DetailsFragmentDirections.actionDetailsFragmentToAddTimeFragment(null,LESSON.id, LESSON.belowProgram)
                it.findNavController().navigate(actionToAddProgram)
            }else{
                //show alert Dialog
            }

        }


        binding.fragmentDetailsImageViewDelete.setOnClickListener {

            val title = resources.getString(R.string.alertDialog_titleDelete)
            val message = resources.getString(R.string.alertDialog_sureDelete)

            showAlert(title,message){
                if(it){
                    viewModel.deleteLesson(LESSON.id)
                    Toast.makeText(context,resources.getString(R.string.toast_successful),Toast.LENGTH_LONG).show()
                    val actionToMain = DetailsFragmentDirections.actionDetailsFragmentToAddEditLessonsFragment()
                    findNavController().navigate(actionToMain)
                }
            }
        }
    }

    fun showAlert(title: String,message: String, response: (Boolean) -> Unit){

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val string = requireActivity().resources


        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setPositiveButton(string.getString(R.string.alertDialog_posButton)) { dialog, id ->
            response(true)
        }
        alertDialogBuilder.setNegativeButton(string.getString(R.string.alertDialog_negButton)){dialog, id ->
            response(false)
        }

        alertDialogBuilder.show()
    }




    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }




}