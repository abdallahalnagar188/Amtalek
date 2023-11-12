package eramo.amtalek.presentation.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentCommentsBottomDialogBinding
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMarketCommentsAdapter
import eramo.amtalek.util.Dummy
import javax.inject.Inject

@AndroidEntryPoint
class CommentsBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentsBottomDialogBinding
    private lateinit var listener: CommentsBottomDialogOnClickListener

    @Inject
    lateinit var rvMarketCommentsAdapter: RvMarketCommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommentsBottomDialogBinding.bind(view)

        binding.ivClose.setOnClickListener {
//            listener.onBottomSheetDialogClick("ss")
            dismiss()
        }

        initRvComments(Dummy.dummyRatingCommentsList())

        binding.tvComments.text = getString(R.string.s_comments, "12")
    }

    private fun initRvComments(data: List<RatingCommentsModel>) {
        binding.rv.adapter = rvMarketCommentsAdapter
        rvMarketCommentsAdapter.submitList(data)
    }

    fun setListener(listener: CommentsBottomDialogOnClickListener) {
        this.listener = listener
    }

    interface CommentsBottomDialogOnClickListener {
        fun onBottomSheetDialogClick(text: String)
    }
}
