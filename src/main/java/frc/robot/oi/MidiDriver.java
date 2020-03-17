/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

/**
 * Add your docs here.
 */
public class MidiDriver {

    //must be in layer A to work

    private Midi midi;

    public MidiSlider
    slider1,
    slider2,
    slider3,
    slider4,
    slider5,
    slider6,
    slider7,
    slider8,
    sliderMain;

    public MidiEncoder 
    encoder1,
    encoder2,
    encoder3,
    encoder4,
    encoder5,
    encoder6,
    encoder7,
    encoder8,
    encoder9,
    encoder10,
    encoder11,
    encoder12,
    encoder13,
    encoder14,
    encoder15,
    encoder16;
    
    public MidiEncoderButton
    encoder1Button,
    encoder2Button,
    encoder3Button,
    encoder4Button,
    encoder5Button,
    encoder6Button,
    encoder7Button,
    encoder8Button,
    encoder9Button,
    encoder10Button,
    encoder11Button,
    encoder12Button,
    encoder13Button,
    encoder14Button,
    encoder15Button,
    encoder16Button;

    public MidiLedButton
    button1,
    button2,
    button3,
    button4,
    button5,
    button6,
    button7,
    button8,
    button9,
    button10,
    button11,
    button12,
    button13,
    button14,
    button15,
    button16,
    button17,
    button18,
    button19,
    button20,
    button21,
    button22,
    button23,
    button24,
    button25,
    button26,
    button27,
    button28,
    button29,
    button30,
    button31,
    button32,
    button33,
    buttonFF,
    buttonRW, 
    buttonLoop, 
    buttonRec, 
    buttonStop, 
    buttonPlay;

    public MidiDriver(Midi midi){
        this.midi = midi;

        //https://media63.musictribe.com/media/PLM/data/docs/P0B3L/X-TOUCH%20COMPACT_QSG_WW.pdf

        //makes sliders
        slider1 = new MidiSlider(midi, 101, 1);
        slider2 = new MidiSlider(midi, 102, 2);
        slider3 = new MidiSlider(midi, 103, 3);
        slider4 = new MidiSlider(midi, 104, 4);
        slider5 = new MidiSlider(midi, 105, 5);
        slider6 = new MidiSlider(midi, 106, 6);
        slider7 = new MidiSlider(midi, 107, 7);
        slider8 = new MidiSlider(midi, 108, 8);
        sliderMain = new MidiSlider(midi, 109, 9);

        //encoders
        encoder1 = new MidiEncoder(midi, 10, 10);
        encoder2 = new MidiEncoder(midi, 11, 11);
        encoder3 = new MidiEncoder(midi, 12, 12);
        encoder4 = new MidiEncoder(midi, 13, 13);
        encoder5 = new MidiEncoder(midi, 14, 14);
        encoder6 = new MidiEncoder(midi, 15, 15);
        encoder7 = new MidiEncoder(midi, 16, 16);
        encoder8 = new MidiEncoder(midi, 17, 17);
        encoder9 = new MidiEncoder(midi, 18, 18);
        encoder10 = new MidiEncoder(midi, 19, 19);
        encoder11 = new MidiEncoder(midi, 20, 20);
        encoder12 = new MidiEncoder(midi, 21, 21);
        encoder13 = new MidiEncoder(midi, 22, 22);
        encoder14 = new MidiEncoder(midi, 23, 23);
        encoder15 = new MidiEncoder(midi, 24, 24);
        encoder16 = new MidiEncoder(midi, 25, 25);

        //encoder buttons
        encoder1Button = new MidiEncoderButton(midi, 0);
        encoder2Button = new MidiEncoderButton(midi, 1);
        encoder3Button = new MidiEncoderButton(midi, 2);
        encoder4Button = new MidiEncoderButton(midi, 3);
        encoder5Button = new MidiEncoderButton(midi, 4);
        encoder6Button = new MidiEncoderButton(midi, 5);
        encoder7Button = new MidiEncoderButton(midi, 6);
        encoder8Button = new MidiEncoderButton(midi, 7);
        encoder9Button = new MidiEncoderButton(midi, 8);
        encoder10Button = new MidiEncoderButton(midi, 9);
        encoder11Button = new MidiEncoderButton(midi, 10);
        encoder12Button = new MidiEncoderButton(midi, 11);
        encoder13Button = new MidiEncoderButton(midi, 12);
        encoder14Button = new MidiEncoderButton(midi, 13);
        encoder15Button = new MidiEncoderButton(midi, 14);
        encoder16Button = new MidiEncoderButton(midi, 15); 
        
        //buttons
        button1 = new MidiLedButton(midi, 40, 25);
        button2 = new MidiLedButton(midi, 41, 26);
        button3 = new MidiLedButton(midi, 42, 27);
        button4 = new MidiLedButton(midi, 43, 28);
        button5 = new MidiLedButton(midi, 44, 29);
        button6 = new MidiLedButton(midi, 45, 30);
        button7 = new MidiLedButton(midi, 46, 31);
        button8 = new MidiLedButton(midi, 47, 32);
        button9 = new MidiLedButton(midi, 48, 33);
        button10 = new MidiLedButton(midi, 16, 1);
        button11 = new MidiLedButton(midi, 17, 2);
        button12 = new MidiLedButton(midi, 18, 3);
        button13 = new MidiLedButton(midi, 19, 4);
        button14 = new MidiLedButton(midi, 20, 5);
        button15 = new MidiLedButton(midi, 21, 6);
        button16 = new MidiLedButton(midi, 22, 7);
        button17 = new MidiLedButton(midi, 23, 8);
        button18 = new MidiLedButton(midi, 24, 9);
        button19 = new MidiLedButton(midi, 25, 10);
        button20 = new MidiLedButton(midi, 26, 11);
        button21 = new MidiLedButton(midi, 27, 12);
        button22 = new MidiLedButton(midi, 28, 13);
        button23 = new MidiLedButton(midi, 29, 14);
        button24 = new MidiLedButton(midi, 30, 15);
        button25 = new MidiLedButton(midi, 31, 16);
        button26 = new MidiLedButton(midi, 32, 17);
        button27 = new MidiLedButton(midi, 33, 18);
        button28 = new MidiLedButton(midi, 34, 19);
        button29 = new MidiLedButton(midi, 35, 10);
        button30 = new MidiLedButton(midi, 36, 21);
        button31 = new MidiLedButton(midi, 37, 22);
        button32 = new MidiLedButton(midi, 38, 23);
        button33 = new MidiLedButton(midi, 39, 24);
        buttonRW = new MidiLedButton(midi, 49, 34);
        buttonFF = new MidiLedButton(midi, 50, 35);
        buttonLoop = new MidiLedButton(midi, 51, 36);
        buttonRec = new MidiLedButton(midi, 52, 37);
        buttonStop = new MidiLedButton(midi, 53, 38);
        buttonPlay = new MidiLedButton(midi, 54, 39);
    }

}
