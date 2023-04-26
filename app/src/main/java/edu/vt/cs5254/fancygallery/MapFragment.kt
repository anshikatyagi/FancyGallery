package edu.vt.cs5254.fancygallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import edu.vt.cs5254.fancygallery.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView


class MapFragment : Fragment() {

    private val vm:MapViewModel by viewModels()
    private val activityVM:MainViewModel by activityViewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "FGBinding is null" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().apply {
            load(context,PreferenceManager.getDefaultSharedPreferences(requireContext()))
            userAgentValue=BuildConfig.APPLICATION_ID
        }
        with(binding.mapView) {
            minZoomLevel = 1.5
            maxZoomLevel = 15.0
            setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
            )
            isVerticalMapRepetitionEnabled = false
            isTilesScaledToDpi = true
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setTileSource(TileSourceFactory.MAPNIK)
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding.mapView) {
            onResume()
            controller.setZoom(vm.zoomLevel)
            controller.setCenter(vm.mapCenter)

        }
        Log.w("shared vm"," ${activityVM.galleryItems.value.size} accessed")
    }

    override fun onPause() {
        super.onPause()
        with(binding.mapView) {
            vm.saveMapState(zoomLevelDouble,mapCenter)
            onPause()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}