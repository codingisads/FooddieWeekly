package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class TotalDaily(
    @SerializedName("CA")
    val cA: CA,
    @SerializedName("CHOCDF")
    val cHOCDF: CHOCDF,
    @SerializedName("CHOLE")
    val cHOLE: CHOLE,
    @SerializedName("ENERC_KCAL")
    val eNERCKCAL: ENERCKCAL,
    @SerializedName("FASAT")
    val fASAT: FASAT,
    @SerializedName("FAT")
    val fAT: FAT,
    @SerializedName("FE")
    val fE: FE,
    @SerializedName("FIBTG")
    val fIBTG: FIBTG,
    @SerializedName("FOLDFE")
    val fOLDFE: FOLDFE,
    @SerializedName("K")
    val k: K,
    @SerializedName("MG")
    val mG: MG,
    @SerializedName("NA")
    val nA: NA,
    @SerializedName("NIA")
    val nIA: NIA,
    @SerializedName("P")
    val p: P,
    @SerializedName("PROCNT")
    val pROCNT: PROCNT,
    @SerializedName("RIBF")
    val rIBF: RIBF,
    @SerializedName("THIA")
    val tHIA: THIA,
    @SerializedName("TOCPHA")
    val tOCPHA: TOCPHA,
    @SerializedName("VITA_RAE")
    val vITARAE: VITARAE,
    @SerializedName("VITB12")
    val vITB12: VITB12,
    @SerializedName("VITB6A")
    val vITB6A: VITB6A,
    @SerializedName("VITC")
    val vITC: VITC,
    @SerializedName("VITD")
    val vITD: VITD,
    @SerializedName("VITK1")
    val vITK1: VITK1,
    @SerializedName("ZN")
    val zN: ZN
)