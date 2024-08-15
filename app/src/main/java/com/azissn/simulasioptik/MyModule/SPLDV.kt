package com.azissn.simulasioptik.MyModule

import kotlin.math.abs

data class Persamaan(val a: Float, val b: Float, val c: Float)

data class Hasil(val a: Float, val b: Float)

fun Persamaan(x1: Float, y1: Float, x2: Float, y2: Float): Persamaan {
    var deltaX = x2 - x1
    var deltaY = y2 - y1
    val dXkoY = deltaX * 1f
    val dXkoY1 = deltaX * -y1
    val dYkoX = deltaY * 1f
    val dYkoX1 = deltaY * -x1
    var a = dYkoX
    var b = -dXkoY
    var c = dYkoX1 - dXkoY1

    if (a < 0) {
        a = -a
        b = -b
        c = -c
    }

    if (a % 2 == 0f && b % 2 == 0f && c % 2 == 0f) {
        a /= 2
        b /= 2
        c /= 2
    }

    return Persamaan(a, b, c)
}

fun SPLDV(pers1: Persamaan, pers2: Persamaan): Hasil {
    val a1 = pers1.a
    val b1 = pers1.b
    val c1 = pers1.c
    val a2 = pers2.a
    val b2 = pers2.b
    val c2 = pers2.c

    var da1: Float? = null
    var db1: Float? = null
    var dc1: Float? = null
    val pengali1: Float
    val pengali2: Float
    val dega1: Float
    val degb1: Float
    val degc1: Float
    val dega2: Float
    val degb2: Float
    val degc2: Float
    val a: Float
    val b: Float

    // Test pengurangan jika koefisien x sama
    if (a1 == a2 || b1 == b2) {
        da1 = a1 - a2
        db1 = b1 - b2
        dc1 = c1 - c2

        if (da1 == 0f && db1 != 0f) {
            if (db1 % 2 == 0f && dc1 % 2 == 0f) {
                da1 /= 2
                db1 /= 2
                dc1 /= 2
            }
        }
    } else if (a1 != a2 || b1 != b2) {
        pengali1 = a2
        pengali2 = a1
        dega1 = a1 * pengali1
        degb1 = b1 * pengali1
        degc1 = c1 * pengali1
        dega2 = a2 * pengali2
        degb2 = b2 * pengali2
        degc2 = c2 * pengali2

        if (dega1 == dega2) {
            da1 = dega1 - dega2
            db1 = degb1 - degb2
            dc1 = degc1 - degc2
        }
    }


    if (abs(db1!!) < abs(dc1!!)) {
        if (dc1 % db1 == 0f) {
            b = -dc1 / db1
            a = -(b1 * b + c1) / a1
        } else {
            // coba-coba, bisa saja terjadi bug
            b = -dc1 / db1
            a = -(b1 * b + c1) / a1
        }
    } else {
        // coba-coba, bisa saja terjadi bug
        b = -dc1 / db1
        a = -(b1 * b + c1) / a1
    }

    return Hasil(a, b)
}

