package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class TotalNutrients(
    @SerializedName("CA")
    val cA: CA,
    @SerializedName("CHOCDF")
    val cHOCDF: CHOCDF,
    @SerializedName("CHOCDF.net")
    val cHOCDFNet: CHOCDFNet,
    @SerializedName("CHOLE")
    val cHOLE: CHOLE,
    @SerializedName("ENERC_KCAL")
    val eNERCKCAL: ENERCKCAL,
    @SerializedName("FAMS")
    val fAMS: FAMS,
    @SerializedName("FAPU")
    val fAPU: FAPU,
    @SerializedName("FASAT")
    val fASAT: FASAT,
    @SerializedName("FAT")
    val fAT: FAT,
    @SerializedName("FATRN")
    val fATRN: FATRN,
    @SerializedName("FE")
    val fE: FE,
    @SerializedName("FIBTG")
    val fIBTG: FIBTG,
    @SerializedName("FOLAC")
    val fOLAC: FOLAC,
    @SerializedName("FOLDFE")
    val fOLDFE: FOLDFE,
    @SerializedName("FOLFD")
    val fOLFD: FOLFD,
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
    @SerializedName("SUGAR")
    val sUGAR: SUGAR,
    @SerializedName("SUGAR.added")
    val sUGARAdded: SUGARAdded,
    @SerializedName("Sugar.alcohol")
    val sugarAlcohol: SugarAlcohol,
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
    @SerializedName("WATER")
    val wATER: WATER,
    @SerializedName("ZN")
    val zN: ZN
)