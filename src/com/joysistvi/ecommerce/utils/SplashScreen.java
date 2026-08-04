/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.utils;

import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;

/**
 *
 * @author myongson
 */
public class SplashScreen {

    public static void splashScreen() throws InterruptedException {

        clearScreen();

        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                              ║");
        System.out.println("║              ███████╗      ██████╗ ██████╗ ███╗   ███╗                       ║");
        System.out.println("║              ██╔════╝     ██╔════╝██╔═══██╗████╗ ████║                       ║");
        System.out.println("║              █████╗       ██║     ██║   ██║██╔████╔██║                       ║");
        System.out.println("║              ██╔══╝       ██║     ██║   ██║██║╚██╔╝██║                       ║");
        System.out.println("║              ███████╗     ╚██████╗╚██████╔╝██║ ╚═╝ ██║                       ║");
        System.out.println("║              ╚══════╝      ╚═════╝ ╚═════╝ ╚═╝     ╚═╝                       ║");
        System.out.println("║                                                                              ║");
        System.out.println("║                  E-COMMERCE SHOPPING CART SYSTEM                             ║");
        System.out.println("║                                                                              ║");
        System.out.println("║                             Version 1.0                                      ║");
        System.out.println("║                                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");

        System.out.println();
        System.out.println("Initializing Application...");
        System.out.println();

        System.out.print("Loading: [");

        for (int i = 0; i < 50; i++) {
            System.out.print(" ");
        }

        System.out.print("] 0%");

        // Move cursor back inside the loading bar
        System.out.print("\rLoading: [");

        for (int i = 0; i <= 50; i++) {

            if (i > 0)
                System.out.print("█");

            int percent = i * 2;

            // Fill remaining spaces
            for (int j = i; j < 50; j++)
                System.out.print(" ");

            System.out.print("] " + percent + "%");

            // Return cursor after "["
            if (i < 50)
                System.out.print("\rLoading: [");

            Thread.sleep(50);
        }

        System.out.println("\n");
        System.out.println("✔ System Ready!");
        Thread.sleep(1200);

        clearScreen();
    }
}
