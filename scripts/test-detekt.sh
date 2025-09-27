#!/bin/bash

# Test script for Detekt configuration
# Copyright 2025 Bir Kelime Bir İşlem Project

echo "🔍 Testing Detekt Configuration for Kelime İşlem..."
echo "=================================================="

# Check if configuration file exists
if [ ! -f "detekt.yml" ]; then
    echo "❌ detekt.yml not found!"
    exit 1
fi
echo "✅ detekt.yml configuration file found"

# Check if baseline exists
if [ ! -f "detekt-baseline.xml" ]; then
    echo "❌ detekt-baseline.xml not found!"
    exit 1
fi
echo "✅ detekt-baseline.xml baseline file found"

# Run Detekt
echo "🚀 Running Detekt analysis..."
./gradlew detekt --no-daemon

if [ $? -eq 0 ]; then
    echo "✅ Detekt analysis passed with baseline!"
else
    echo "❌ Detekt analysis failed"
    exit 1
fi

# Check reports were generated
echo "📊 Checking generated reports..."
if [ -f "build/reports/detekt/detekt.xml" ]; then
    echo "✅ XML report generated"
else
    echo "❌ XML report missing"
fi

if [ -f "build/reports/detekt/detekt.html" ]; then
    echo "✅ HTML report generated"
else
    echo "❌ HTML report missing"
fi

if [ -f "build/reports/detekt/detekt.sarif" ]; then
    echo "✅ SARIF report generated (for GitHub Security)"
else
    echo "❌ SARIF report missing"
fi

echo "🎉 Detekt configuration test completed successfully!"
echo "📍 Reports available at: build/reports/detekt/"
echo "🔧 Configuration: detekt.yml"
echo "📝 Baseline: detekt-baseline.xml (allows existing issues)"