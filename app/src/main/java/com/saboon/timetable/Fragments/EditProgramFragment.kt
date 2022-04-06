package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.saboon.timetable.R
import com.saboon.timetable.ViewModels.EditProgViewModel
import com.saboon.timetable.databinding.FragmentEditProgramBinding
import java.text.SimpleDateFormat
import java.util.*


class EditProgramFragment : Fragment(){


    private var _binding: FragmentEditProgramBinding? = null
    private val binding get() = _binding!!


    lateinit var viewModel: EditProgViewModel

    lateinit var programName: String
    lateinit var programID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit_program, container, false)

        _binding = FragmentEditProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: viewde olusturulma tarihi gibi bilgileri goster

        viewModel = ViewModelProvider(this).get(EditProgViewModel::class.java)


        arguments?.let {
            if (!it.isEmpty){
                EditProgramFragmentArgs.fromBundle(it).programID.let {
                    programID = it
                    viewModel.getProgramFromSQLite(it)
                }

            }
        }


        binding.fragmentEditProgEditTextProgramName.setOnClickListener{

            val currentProgramName = binding.fragmentEditProgEditTextProgramName.text.toString()
            showChangeValueAlert("Edit", "Change Program Name", currentProgramName){newName ->
                if (newName != "null"){
                    binding.fragmentEditProgEditTextProgramName.setText(newName)
                    //viewModel icerisinde tarihte guncelleniyor
                    viewModel.updateProgramName(programID, newName)
                    //refresh
                    viewModel.getProgramFromSQLite(programID)

                }
            }
        }

        binding.fragmenEditProgTextViewEditProgram.setOnClickListener {
            val actionToBack = EditProgramFragmentDirections.actionEditProgramFragmentToManageProgramFragment()
            it.findNavController().navigate(actionToBack)
        }


        binding.fragmentEditProgImageViewDelete.setOnClickListener {

            showDeleteAlert(resources.getString(R.string.alertDialog_titleDelete),resources.getString(R.string.alertDialog_sureDelete)){response->
                if (response){
                    viewModel.deleteProgram(programID)
                    val actionToManageProg = EditProgramFragmentDirections.actionEditProgramFragmentToManageProgramFragment()
                    it.findNavController().navigate(actionToManageProg)
                }
            }

        }

        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.program.observe(viewLifecycleOwner,Observer{
            it?.let {
                binding.fragmentEditProgEditTextProgramName.setText(it.name)

                //gelen tarih verisi soyle dd.MM.yyyy-HH:mm:ss
                //sadece tarih kismini almak icin parcalayip dd MMMM yyyy formatina ceviriyoruz

                val dateCreated = SimpleDateFormat("dd MMMM yyyy").format(SimpleDateFormat("dd.MM.yyyy").parse(it.dateEdited.split("-")[0]))

                binding.textViewDateCreated.setText("${resources.getText(R.string.textView_dateAdded)}: ${dateCreated}")
            }
        })
    }


    fun showChangeValueAlert(title: String, message: String, currentString: String, response: (String) -> Unit){

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val string = requireActivity().resources
        val inflater = requireActivity().layoutInflater

        //custom viewi getir
        val view = inflater.inflate(R.layout.edit_text_dialog_layout, null)


        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        //custom viewi set et
        alertDialogBuilder.setView(view)

        //custom viewdeki gorunumleri tanimla
        val editText: EditText =  view.findViewById(R.id.alertDialogEditText)
        val textView: TextView = view.findViewById(R.id.alertDialogTextViewcurrentString)

        textView.setText(currentString)
        alertDialogBuilder.setPositiveButton(string.getString(R.string.alertDialog_saveButton)) { dialog, id ->
            //eger kullanici kaydete basarsa edit texte yazdigi stringle birlikte true gonder
            response(editText.text.toString())
        }
        alertDialogBuilder.setNegativeButton(string.getString(R.string.alertDialog_cancelButton)){ dialog, id ->
            response("null")
        }

        alertDialogBuilder.show()
    }

    fun showDeleteAlert(title: String,message: String, response: (Boolean) -> Unit){

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

    override fun onDestroy() {
        super.onDestroy()

        _binding = null

    }


}