#!/usr/bin/env kotlin

// Import necessary HtmlUnit classes
import org.htmlunit.BrowserVersion
import org.htmlunit.WebClient
import org.htmlunit.html.*
import java.util.concurrent.TimeUnit

fun main() {
    // Create WebClient instance with Chrome-like browser version
    val webClient = WebClient(BrowserVersion.CHROME)
    
    try {
        // Configure WebClient settings
        webClient.options().apply {
            isJavaScriptEnabled = true
            isCssEnabled = false
            setIsThrowExceptionOnFailingStatusCode(false)
            setIsThrowExceptionOnScriptError(false)
            setTimeout(10000)
        }
        
        println("Navigating to multitransfer.ru...")
        
        // Navigate to the website
        val page = webClient.getPage<HtmlPage>("https://multitransfer.ru")
        
        println("Page loaded successfully")
        
        // Find and click on Türkiye selection
        // Since we don't know the exact element structure for selecting Türkiye,
        // I'll look for elements that might represent country selection
        val countryElements = page.getByXPath<HtmlElement>("//*[contains(text(), 'Türkiye') or contains(text(), 'Turkey') or contains(@*, 'turkey') or contains(@*, 'türk')]")

        var turkeySelected = false
        for (element in countryElements) {
            try {
                if (element is HtmlAnchor || element.tagName.lowercase() == "a") {
                    element.click()
                    turkeySelected = true
                    break
                } else if (element is HtmlDivision || element.tagName.lowercase() in listOf("div", "span", "button")) {
                    element.click()
                    turkeySelected = true
                    break
                }
            } catch (e: Exception) {
                println("Could not click on element: ${e.message}")
                continue
            }
        }
        
        if (!turkeySelected) {
            println("Could not find and select Türkiye directly, trying other approaches...")
            
            // Alternative: Look for dropdowns or radio buttons that might contain country options
            val selects = page.getElementsByTagName("select")
            for (select in selects) {
                val options = select.getElementsByTagName("option")
                for (option in options) {
                    if (option.textContent.contains("Türkiye", ignoreCase = true)) {
                        (select as HtmlSelect).setSelectedAttribute(option, true)
                        turkeySelected = true
                        break
                    }
                }
                if (turkeySelected) break
            }
        }

        if (!turkeySelected) {
            println("Warning: Could not find and select Türkiye")
        } else {
            println("Successfully selected Türkiye")
            
            // Wait for potential page reload after country selection
            webClient.waitForBackgroundJavaScript(5000)
            TimeUnit.SECONDS.sleep(2)
        }
        
        // Find the input field with the specific attributes mentioned
        val inputField = page.getElementById<HtmlTextInput>(":r8:")
        
        if (inputField != null) {
            println("Found the input field, filling with 50000...")
            inputField.type("50000")
            println("Value entered: ${inputField.valueAttribute}")
        } else {
            println("Could not find the specific input field by ID :r8:, searching by other attributes...")
            
            // Search by placeholder
            val inputsByPlaceholder = page.getByXPath<HtmlTextInput>("//input[@placeholder='0 RUB']")
            if (inputsByPlaceholder.isNotEmpty()) {
                val targetInput = inputsByPlaceholder[0]
                println("Found input field by placeholder, filling with 50000...")
                targetInput.type("50000")
                println("Value entered: ${targetInput.valueAttribute}")
            } else {
                println("Could not find the input field by placeholder either")
            }
        }
        
        // Find and click the "Выбрать банк Get Money Global" button
        val chooseBankButtons = page.getByXPath<HtmlElement>(
            "//div[contains(@class, 'home') and .//text()[contains(., 'Get Money Global')]]" +
            "//div[@role='button' and .//div[contains(@class, 'choose') and contains(., 'Выбрать')]]"
        )
        
        if (chooseBankButtons.isEmpty()) {
            // Alternative search method
            val alternativeButton = page.getFirstByXPath<HtmlElement>(
                "//div[contains(@aria-label, 'Выбрать банк Get Money Global') or contains(@class, 'home')]//div[@role='button']"
            )
            
            if (alternativeButton != null) {
                println("Found bank selection button, clicking...")
                alternativeButton.click()
                println("Clicked the bank selection button")
            } else {
                println("Could not find the bank selection button")
            }
        } else {
            println("Found bank selection button, clicking...")
            chooseBankButtons[0].click()
            println("Clicked the bank selection button")
        }
        
        // Wait for any JavaScript actions to complete
        webClient.waitForBackgroundJavaScript(5000)
        TimeUnit.SECONDS.sleep(2)
        
        println("Script execution completed")
        
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
        e.printStackTrace()
    } finally {
        // Close the WebClient to free resources
        webClient.close()
    }
}

// Run the main function
main()