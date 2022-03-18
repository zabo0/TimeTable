package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    private lateinit var lessonID: String
    private lateinit var belowProgramID : String

    lateinit var createdLesson: ModelLesson

    //yeni kayit icinmi gelindi yoksa eski kayitmi gosteriliyor diye kotrol icin
    //checkTheChanges() fonksiyonu icin gerekli
    private var isNewLesson = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //eger geri tusuna basilirsa burasi calisir
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
                findNavController().navigate(actionToBack)
            }
        })
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
                DetailsFragmentArgs.fromBundle(it).belowProgramID?.let {
                    belowProgramID = it
                }

                DetailsFragmentArgs.fromBundle(it).selectedLessonID?.let {
                    lessonID = it
                    isNewLesson = false
                    viewModel.getDataFromSQLite(it)
                }?: run{
                    //eger yeni bir ders ekleniyorsa burayi calistir
                    createNewLesson()
                }


            }
        }

        binding.fragmentDetailsRecyclerViewProgramRecycler.layoutManager = LinearLayoutManager(context)

        binding.fragmentDetailsRecyclerViewProgramRecycler.adapter = recyclerAdapter




        binding.fragmentDetailsTextViewLessonDetails.setOnClickListener {
            val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
            it.findNavController().navigate(actionToBack)
        }

        binding.fragmentDetailsTextViewAddProgram.setOnClickListener {

            if (lessonID != "null"){
                val actionToAddProgram = DetailsFragmentDirections.actionDetailsFragmentToAddProgramFragment(null,lessonID, belowProgramID)
                it.findNavController().navigate(actionToAddProgram)
            }else{
                //show alert Dialog
            }

        }


        binding.fragmentDetailsEditTextLessonName.setOnFocusChangeListener { view, b ->

            //buranin olayi eger editText focusu kaybettiyse edittexte yazilani kontrol et
            //eger databasedeki ile ayni degilse yazilan degeri database kaydet
            //ayni ise hic bir sey yapma
            if (!b){
                binding.fragmentDetailsEditTextLessonName.text?.let {

                    viewModel.getLessonFromSQLite(lessonID){ lesson ->
                        if (it.toString() != lesson.lessonName && it.toString() != ""){
                            viewModel.updateLessonName(lesson.id, it.trim().toString())
                            isNewLesson = false
                            // TODO: burada focus ustundeiken kullanici geriye bastiginda veri henuz databaseye kaydedilemeden main fragmente gidiyor ve orada eski veriyi cekmis oluyor
                        }
                    }
                }
            }
        }

        binding.fragmentDetailsEditTextLecturerName.setOnFocusChangeListener { view, b ->
            //buranin olayi eger editText focusu kaybettiyse edittexte yazilani kontrol et
            //eger databasedeki ile ayni degilse yazilan degeri database kaydet
            //ayni ise hic bir sey yapma
            if (!b){
                binding.fragmentDetailsEditTextLecturerName.text?.let {
                    viewModel.getLessonFromSQLite(lessonID){lesson ->
                        if (it.toString() != lesson.lecturerName && it.toString() != ""){
                            viewModel.updateLecturerName(lesson.id, it.trim().toString())
                            isNewLesson = false
                            // TODO: burada focus ustundeiken kullanici geriye bastiginda veri henuz databaseye kaydedilemeden main fragmente gidiyor ve orada eski veriyi cekmis oluyor
                        }
                    }
                }
            }
        }

        binding.fragmentDetailsImageViewDelete.setOnClickListener {



            if (isNewLesson){
                viewModel.deleteLesson(lessonID)
                val actionToMain = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
                findNavController().navigate(actionToMain)
            }else{
                val title = resources.getString(R.string.alertDialog_titleDelete)
                val message = resources.getString(R.string.alertDialog_sureDelete)

                showAlert(title,message){
                    if(it){
                        viewModel.deleteLesson(lessonID)
                        Toast.makeText(context,resources.getString(R.string.toast_successful),Toast.LENGTH_LONG).show()
                        val actionToMain = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
                        findNavController().navigate(actionToMain)
                    }
                }
            }



        }

        observeLiveData()


    }


    fun createNewLesson(){

        //bu fonksiyonun amaci sudur:
        //kullanici ana sayfadan buraya yeni bir ders eklemek icin geliyorsa bu fragment acildiginda direkt yeni bir ders olusturulur
        //ancak bu olusturulan dersin nullable fieldlari database e sonradan degistirilmek uzere null olarak kaydedilir
        //kullanici bu fragmentte her ekledigi deger icin databade guncellenir

        val id = IDGenerator().generateLessonID()
        val dateAdded = SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Calendar.getInstance().time)
        val defaultColor = "#C62910"
        val defaultAbsenteeism = "0"
        val belowProgram = belowProgramID

        createdLesson = ModelLesson(id,dateAdded,null,null,defaultColor,defaultAbsenteeism,belowProgram)
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