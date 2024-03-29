package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saboon.timetable.Adapters.ManageProgRecyclerAdapter
import com.saboon.timetable.Models.ModelProgram
import com.saboon.timetable.R
import com.saboon.timetable.Utils.IDGenerator
import com.saboon.timetable.ViewModels.ManageProgViewModel
import com.saboon.timetable.databinding.FragmentManageProgramBinding
import java.text.SimpleDateFormat
import java.util.*


class ManageProgramFragment : Fragment() {


    private var _binding: FragmentManageProgramBinding? = null
    private val binding get() = _binding!!


    lateinit var viewModel: ManageProgViewModel

    lateinit var recyclerAdapter: ManageProgRecyclerAdapter


    // TODO: search bari bitir

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //requireActivity() methodunu dogru cagirabilmek icin bunu burada initialize ediyorum
        recyclerAdapter = ManageProgRecyclerAdapter(arrayListOf(), requireActivity())
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_manage_program, container, false)

        _binding = FragmentManageProgramBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ManageProgViewModel::class.java)
        viewModel.getAllProgramsFromDatabase()

        binding.fragmentManageProgRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.fragmentManageProgRecyclerView.adapter = recyclerAdapter
        binding.fragmentManageProgRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.fragmentManageProgImageViewAdd.setOnClickListener{
            alerDialog()
        }

        binding.fragmentManageProgTextViewManagePrograms.setOnClickListener{
            val actionToBack = ManageProgramFragmentDirections.actionManageProgramFragmentToMainFragment()
            it.findNavController().navigate(actionToBack)
        }

        binding.fragmentManageProgEditTextSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                if (p0.toString() != ""){
                    val search = "%${p0.toString()}%"
                    viewModel.getAllProgByFilter(search)
                }else{
                    viewModel.getAllProgramsFromDatabase()
                }

            }

        })


        observeData()
    }


    fun observeData(){

        viewModel.programs.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fragmentManageProgRecyclerView.visibility = View.VISIBLE
                recyclerAdapter.updateList(it)
                binding.manageLoadingProgressBar.visibility = View.GONE
                binding.manageEmptyText.visibility = View.GONE
                binding.manageErrorText.visibility = View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentManageProgRecyclerView.visibility = View.GONE
                    binding.manageLoadingProgressBar.visibility = View.VISIBLE
                    binding.manageEmptyText.visibility = View.GONE
                    binding.manageErrorText.visibility = View.GONE
                }else{
                    binding.manageLoadingProgressBar.visibility = View.GONE
                }
            }
        })
        viewModel.addNewText.observe(viewLifecycleOwner,Observer {
            it?.let {
                if(it){
                    binding.fragmentManageProgRecyclerView.visibility = View.GONE
                    binding.manageLoadingProgressBar.visibility = View.GONE
                    binding.manageEmptyText.visibility = View.VISIBLE
                    binding.manageErrorText.visibility = View.GONE
                }else{
                    binding.manageEmptyText.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentManageProgRecyclerView.visibility = View.GONE
                    binding.manageLoadingProgressBar.visibility = View.GONE
                    binding.manageEmptyText.visibility = View.GONE
                    binding.manageErrorText.visibility = View.VISIBLE
                }else{
                    binding.manageErrorText.visibility = View.GONE
                }
            }
        })
    }



    fun alerDialog(){

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val inflater = requireActivity().layoutInflater
        val dialogLayout = inflater.inflate(R.layout.manage_dialog_view, null)
        val programNameText = dialogLayout.findViewById<EditText>(R.id.dialog_progName)

       with(alertDialogBuilder){
           setTitle("Add Program")
           setPositiveButton("Save"){dialog, which ->
               val programName = programNameText.text.trimEnd().toString()
               val dateAdded = SimpleDateFormat("dd.MM.yyyy-HH:mm:ss").format(Calendar.getInstance().time)
               val dateEdited = SimpleDateFormat("dd.MM.yyyy-HH:mm:ss").format(Calendar.getInstance().time)
               val id = IDGenerator().generateProgramID(programName)
               val newProgram = ModelProgram(id,programName,dateAdded,dateEdited)
               viewModel.storeProgramInDatabase(newProgram){callback ->
                   if (callback){
                       viewModel.getAllProgramsFromDatabase()
                   }
               }

           }
           setNegativeButton("Cancel") {dialog, which ->
                dialog.cancel()
           }
           setView(dialogLayout)
           show()
       }
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}