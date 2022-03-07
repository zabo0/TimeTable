package com.saboon.timetable.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saboon.timetable.Adapters.DetailsRecyclerAdapter
import com.saboon.timetable.Models.ModelLesson
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


    private lateinit var lessonID: String
    private lateinit var belowProgramID : String

    lateinit var createdLesson: ModelLesson


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        //viewModel.refreshData()

        arguments?.let {
            if(it != null){
                DetailsFragmentArgs.fromBundle(it).selectedLessonID?.let {
                    lessonID = it
                    viewModel.getDataFromSQLite(it)
                    showButtons(false)
                } ?: run {
                    activateAddProgram(false)
                }

                DetailsFragmentArgs.fromBundle(it).belowProgramID?.let {
                    belowProgramID = it
                }

            }
        }




        binding.fragmentDetailsTextViewLessonDetails.setOnClickListener {
            val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
            it.findNavController().navigate(actionToBack)
        }


        binding.fragmentDetailsButtonSave.setOnClickListener{
            storeData()
            showButtons(false)
            activateAddProgram(true)
        }


        binding.fragmentDetailsTextViewAddProgram.setOnClickListener {
            //storeData()
            if (lessonID != null || lessonID != "null"){
                val actionToAddProgram = DetailsFragmentDirections.actionDetailsFragmentToAddProgramFragment(null,lessonID, belowProgramID)
                it.findNavController().navigate(actionToAddProgram)
            }else{
                //show alert Dialog
            }

        }


        binding.fragmentDetailsRecyclerViewProgramRecycler.layoutManager = LinearLayoutManager(context)

        binding.fragmentDetailsRecyclerViewProgramRecycler.adapter = recyclerAdapter


        observeLiveData()

    }


    fun storeData(){
        val lessonName = binding.fragmentDetailsEditTextLessonName.text.toString()
        val id = IDGenerator().generateLessonID(lessonName)
        val dateAdded = SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Calendar.getInstance().time)
        val lecturerName = binding.fragmentDetailsEditTextLecturerName.text.toString()
        val color = "#C62910"
        val absenteeism = "2"
        val belowProgram = belowProgramID

        createdLesson = ModelLesson(id,dateAdded,lessonName,lecturerName,color,absenteeism,belowProgram)
        lessonID = createdLesson.id

        viewModel.storeLessonInDatabase(createdLesson)
    }


    fun observeLiveData(){
        viewModel.lessonName.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsEditTextLessonName.setText(it)
        })
        viewModel.lecturerName.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsEditTextLecturerName.setText(it)
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

    fun showButtons(state: Boolean){
        if(state){
            binding.fragmentDetailsLinearLayoutButtons.visibility = View.VISIBLE
            binding.fragmentDetailsEditTextLessonName.isFocusable = true
            binding.fragmentDetailsEditTextLecturerName.isFocusable = true
        }else{
            binding.fragmentDetailsLinearLayoutButtons.visibility = View.GONE
            binding.fragmentDetailsEditTextLessonName.isFocusable = false
            binding.fragmentDetailsEditTextLecturerName.isFocusable = false
        }
    }

    fun activateAddProgram(state: Boolean){
        if (state){
            binding.fragmentDetailsLinearLayoutAddProgram.isEnabled = true
        }else{
            binding.fragmentDetailsLinearLayoutAddProgram.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}