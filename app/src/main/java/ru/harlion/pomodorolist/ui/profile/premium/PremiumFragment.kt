package ru.harlion.pomodorolist.ui.profile.premium


import android.os.Bundle
import android.view.View
import android.widget.Button
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.google.android.material.snackbar.Snackbar
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.AppApplication
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.data.billing.BillingClientWrapper
import ru.harlion.pomodorolist.databinding.FragmentPremiumBinding
import ru.harlion.pomodorolist.ui.dialogs.AlertDialogBase
import ru.harlion.pomodorolist.utils.Prefs


class PremiumFragment : BindingFragment<FragmentPremiumBinding>(FragmentPremiumBinding::inflate),
    BillingClientWrapper.OnPurchaseListener {

    lateinit var billingClientWrapper: BillingClientWrapper
    private lateinit var prefs: Prefs

    private val purchaseButtonsMap: Map<String, Button> by lazy(LazyThreadSafetyMode.NONE) {
        mapOf(
            "premium_sub_month" to binding.premiumMonth,
            //  "premium_sub_year" to binding.premiumYear
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        billingClientWrapper = (requireActivity().application as AppApplication).clientWrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())

        billingClientWrapper.onPurchaseListener = this

        displayProducts()

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.promoIn.setOnClickListener {
            AlertDialogBase(requireContext()).apply {
                setTitle(getString(R.string.promo_in))
                setEditText("", "")
                setPositiveButton(getString(R.string.yes)) {
                    if (newText.toString() == "google_test"
                        || newText.toString() == "lev_dev") {
                        prefs.isPremium = true
                        Snackbar.make(binding.root, getString(R.string.promocode_completed), Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(binding.root, getString(R.string.promocode_not_completed), Snackbar.LENGTH_SHORT).show()
                    }
                }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        billingClientWrapper.onPurchaseListener = null
    }

    override fun onPurchaseSuccess(purchase: Purchase?) {
        if (purchase != null) {
            prefs.isPremium = true
        }
    }

    override fun onPurchaseFailure(error: BillingClientWrapper.Error) {
        //handle error or user cancelation
    }

    private fun displayProducts() {
        billingClientWrapper.queryProducts(object : BillingClientWrapper.OnQueryProductsListener {
            override fun onSuccess(products: List<SkuDetails>) {
                products.forEach { product ->
                    purchaseButtonsMap[product.sku]?.apply {
                        text = "${product.description} for ${product.price}"
                        setOnClickListener {
                            billingClientWrapper.purchase(
                                requireActivity(),
                                product
                            ) //will be declared below
                        }
                    }
                }
            }

            override fun onFailure(error: BillingClientWrapper.Error) {
                //handle error
            }
        }, purchaseButtonsMap.keys.toList())
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}