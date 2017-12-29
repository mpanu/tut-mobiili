<CsoundSynthesizer>
<CsOptions>
-o dac -+rtmidi=null -d -+msg_color=0 -M0 -m0
</CsOptions>
<CsInstruments>
nchnls=2
0dbfs=1
ksmps=32
sr = 48000

instr 1
;x,y,z from accelerometer
kaccelX chnget "accelerometerX" 
kaccelY chnget "accelerometerY" 
kaccelZ chnget "accelerometerZ" 

;portamento
kaccelX port kaccelX, 0.001
kaccelY port kaccelY, 0.001
kaccelZ port kaccelZ, 0.001

;sum x,y and multiply
kpch = 80 + (kaccelX + kaccelY + 20) * 40

;pitch to software bus
chnset kpch, "pitch"

;oscillator
a1 vco2 .5, kpch

;reverb
aL, aR reverbsc a1, a1, .72, 5000

out aL, aR

endin

</CsInstruments>
<CsScore>
f1 0 16384 10 1

i1 0 360000
 
</CsScore>
</CsoundSynthesizer>
